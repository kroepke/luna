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

package org.classdump.luna.compiler.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.classdump.luna.compiler.analysis.types.Type;
import org.classdump.luna.compiler.analysis.types.TypeSeq;
import org.classdump.luna.compiler.ir.AbstractVal;
import org.classdump.luna.compiler.ir.MultiVal;
import org.classdump.luna.compiler.ir.PhiVal;
import org.classdump.luna.compiler.ir.Val;
import org.classdump.luna.compiler.ir.Var;

public class TypeInfo {

  private final Map<AbstractVal, Type> types;
  private final Map<MultiVal, TypeSeq> multiTypes;
  private final Map<Var, Boolean> vars;
  private final TypeSeq returnType;

  protected TypeInfo(
      Map<AbstractVal, Type> types,
      Map<MultiVal, TypeSeq> multiTypes,
      Map<Var, Boolean> vars,
      TypeSeq returnType) {

    this.types = Objects.requireNonNull(types);
    this.multiTypes = Objects.requireNonNull(multiTypes);
    this.vars = Objects.requireNonNull(vars);
    this.returnType = Objects.requireNonNull(returnType);
  }

  public static TypeInfo of(
      Map<Val, Type> valTypes,
      Map<PhiVal, Type> phiValTypes,
      Map<MultiVal, TypeSeq> multiValTypes,
      Set<Var> vars, Set<Var> reifiedVars,
      TypeSeq returnType) {

    Map<AbstractVal, Type> types = new HashMap<>();
    Map<MultiVal, TypeSeq> multiTypes = new HashMap<>();
    Map<Var, Boolean> vs = new HashMap<>();

    for (Map.Entry<Val, Type> e : valTypes.entrySet()) {
      types.put(e.getKey(), e.getValue());
    }

    for (Map.Entry<PhiVal, Type> e : phiValTypes.entrySet()) {
      types.put(e.getKey(), e.getValue());
    }

    for (Map.Entry<MultiVal, TypeSeq> e : multiValTypes.entrySet()) {
      multiTypes.put(e.getKey(), e.getValue());
    }

    for (Var v : vars) {
      vs.put(v, reifiedVars.contains(v));
    }
    for (Var v : reifiedVars) {
      if (!vs.containsKey(v)) {
        throw new IllegalStateException("Reified variable " + v + " not found");
      }
    }

    return new TypeInfo(types, multiTypes, vs, returnType);
  }

  public Iterable<AbstractVal> vals() {
    return types.keySet();
  }

  public Iterable<MultiVal> multiVals() {
    return multiTypes.keySet();
  }

  public Type typeOf(AbstractVal v) {
    Objects.requireNonNull(v);

    Type t = types.get(v);
    if (t == null) {
      throw new NoSuchElementException("No type information for " + v);
    } else {
      return t;
    }
  }

  public TypeSeq typeOf(MultiVal mv) {
    Objects.requireNonNull(mv);

    TypeSeq tseq = multiTypes.get(mv);
    if (tseq == null) {
      throw new NoSuchElementException("No type information for multi-value " + mv);
    } else {
      return tseq;
    }
  }

  public Iterable<Var> vars() {
    return vars.keySet();
  }

  public boolean isReified(Var v) {
    Objects.requireNonNull(v);

    Boolean r = vars.get(v);
    if (r != null) {
      return r.booleanValue();
    } else {
      throw new NoSuchElementException("Variable not found: " + v);
    }
  }

  public TypeSeq returnType() {
    return returnType;
  }

}
