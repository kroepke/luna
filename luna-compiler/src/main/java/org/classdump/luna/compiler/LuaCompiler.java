/*
 * Copyright 2016 Miroslav Janíček
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.classdump.luna.compiler;

import java.io.ByteArrayInputStream;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.classdump.luna.compiler.analysis.DependencyAnalyser;
import org.classdump.luna.compiler.analysis.DependencyInfo;
import org.classdump.luna.compiler.analysis.LivenessAnalyser;
import org.classdump.luna.compiler.analysis.LivenessInfo;
import org.classdump.luna.compiler.analysis.SlotAllocInfo;
import org.classdump.luna.compiler.analysis.SlotAllocator;
import org.classdump.luna.compiler.analysis.TypeInfo;
import org.classdump.luna.compiler.analysis.Typer;
import org.classdump.luna.compiler.gen.BytecodeEmitter;
import org.classdump.luna.compiler.gen.ClassNameTranslator;
import org.classdump.luna.compiler.gen.CompiledClass;
import org.classdump.luna.compiler.gen.SuffixingClassNameTranslator;
import org.classdump.luna.compiler.gen.asm.ASMBytecodeEmitter;
import org.classdump.luna.compiler.tf.BranchInliner;
import org.classdump.luna.compiler.tf.CPUAccounter;
import org.classdump.luna.compiler.tf.CodeSimplifier;
import org.classdump.luna.compiler.tf.ConstFolder;
import org.classdump.luna.compiler.tf.DeadCodePruner;
import org.classdump.luna.parser.ParseException;
import org.classdump.luna.parser.Parser;
import org.classdump.luna.parser.TokenMgrError;
import org.classdump.luna.parser.analysis.NameResolver;
import org.classdump.luna.parser.ast.Chunk;
import org.classdump.luna.util.ByteVector;

/**
 * A Lua-to-Java-bytecode compiler.
 */
public class LuaCompiler {

  private final CompilerSettings settings;

  /**
   * Constructs a new compiler instance with the given settings.
   *
   * @param settings the settings, must not be {@code null}
   * @throws NullPointerException if {@code settings} is {@code null}
   */
  public LuaCompiler(CompilerSettings settings) {
    this.settings = Objects.requireNonNull(settings);
  }

  /**
   * Constructs a new compiler instance with
   * {@linkplain CompilerSettings#defaultSettings() default settings}.
   */
  public LuaCompiler() {
    this(CompilerSettings.defaultSettings());
  }

  private static Chunk parse(String sourceText) throws ParseException, TokenMgrError {
    ByteArrayInputStream bais = new ByteArrayInputStream(sourceText.getBytes());
    Parser parser = new Parser(bais);
    return parser.Chunk();
  }

  private static Module translate(Chunk chunk) {
    chunk = NameResolver.resolveNames(chunk);
    return IRTranslator.translate(chunk);
  }

  /**
   * Returns the settings used in this compiler instance.
   *
   * @return the settings used by this compiler
   */
  public CompilerSettings settings() {
    return settings;
  }

  private Iterable<IRFunc> sortTopologically(Module module) {
    // TODO
    return module.fns();
  }

  private IRFunc optimise(IRFunc fn) {
    IRFunc oldFn;

    do {
      oldFn = fn;

      TypeInfo typeInfo = Typer.analyseTypes(fn);

      fn = CPUAccounter.collectCPUAccounting(fn);
      fn = BranchInliner.inlineBranches(fn, typeInfo);

      if (settings.constFolding()) {
        fn = ConstFolder.replaceConstOperations(fn, typeInfo);
        LivenessInfo liveness = LivenessAnalyser.computeLiveness(fn);
        fn = DeadCodePruner.pruneDeadCode(fn, typeInfo, liveness);
      }

      fn = CodeSimplifier.pruneUnreachableCode(fn);
      fn = CodeSimplifier.mergeBlocks(fn);

    } while (!oldFn.equals(fn));

    return fn;
  }

  ProcessedFunc processFunction(IRFunc fn) {
    fn = CPUAccounter.insertCPUAccounting(fn);
    fn = optimise(fn);

    SlotAllocInfo slots = SlotAllocator.allocateSlots(fn);
    TypeInfo types = Typer.analyseTypes(fn);
    DependencyInfo deps = DependencyAnalyser.analyse(fn);

    return new ProcessedFunc(fn, slots, types, deps);
  }

  private Iterable<ProcessedFunc> processModule(Module m) {
    Map<FunctionId, ProcessedFunc> pfs = new HashMap<>();

    for (IRFunc fn : sortTopologically(m)) {
      ProcessedFunc pf = processFunction(fn);
      pfs.put(fn.id(), pf);
    }

    ProcessedFunc main = pfs.get(FunctionId.root());
    assert (main != null);

    Set<ProcessedFunc> result = new HashSet<>();
    Deque<ProcessedFunc> open = new ArrayDeque<>();

    // only add functions reachable from main
    open.add(main);
    while (!open.isEmpty()) {
      ProcessedFunc pf = open.pop();
      if (!result.contains(pf)) {
        result.add(pf);
        for (FunctionId id : pf.deps.nestedRefs()) {
          open.push(pfs.get(id));
        }
      }
    }

    return result;
  }

  private CompiledClass compileFunction(ProcessedFunc pf, String sourceFileName,
      String rootClassName) {
    ClassNameTranslator classNameTranslator = new SuffixingClassNameTranslator(rootClassName);
    BytecodeEmitter emitter = new ASMBytecodeEmitter(
        pf.fn, pf.slots, pf.types, pf.deps,
        settings, classNameTranslator,
        sourceFileName);
    return emitter.emit();
  }

  /**
   * Compiles the Lua source string {@code sourceText} into Java bytecode, giving the main
   * class the name {@code rootClassName}, and using {@code sourceFileName} as the name
   * of the source file (for debugging information),
   *
   * @param sourceText source text, must not be {@code null}
   * @param sourceFileName file name of the source, must not be {@code null}
   * @param rootClassName class name of the main class, must not be {@code null}
   * @return {@code sourceText} compiled into a loadable module
   * @throws NullPointerException if {@code sourceText}, {@code sourceFileName} or {@code
   * rootClassName} is {@code null}
   * @throws TokenMgrError when {@code sourceText} cannot be lexically analysed following the Lua
   * lexical rules
   * @throws ParseException when {@code sourceText} cannot be parsed following the Lua grammar
   */
  public CompiledModule compile(String sourceText, String sourceFileName, String rootClassName)
      throws ParseException, TokenMgrError {

    Objects.requireNonNull(sourceText);
    Chunk ast = parse(sourceText);
    Module module = translate(ast);

    Iterable<ProcessedFunc> pfs = processModule(module);

    Map<String, ByteVector> classMap = new HashMap<>();
    String mainClass = null;
    for (ProcessedFunc pf : pfs) {
      CompiledClass cc = compileFunction(pf, sourceFileName, rootClassName);

      if (pf.fn.id().isRoot()) {
        assert (mainClass == null);
        mainClass = cc.name();
      }

      classMap.put(cc.name(), cc.bytes());
    }

    if (mainClass == null) {
      throw new IllegalStateException("Module main class not found");
    }

    return new CompiledModule(Collections.unmodifiableMap(classMap), mainClass);
  }

  private static class ProcessedFunc {

    public final IRFunc fn;
    public final SlotAllocInfo slots;
    public final TypeInfo types;
    public final DependencyInfo deps;

    private ProcessedFunc(IRFunc fn, SlotAllocInfo slots, TypeInfo types, DependencyInfo deps) {
      this.fn = Objects.requireNonNull(fn);
      this.slots = Objects.requireNonNull(slots);
      this.types = Objects.requireNonNull(types);
      this.deps = Objects.requireNonNull(deps);
    }

  }

}
