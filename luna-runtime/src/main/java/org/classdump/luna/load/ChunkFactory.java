package org.classdump.luna.load;

import org.classdump.luna.Variable;
import org.classdump.luna.runtime.LuaFunction;

/**
 * Convenience class to instantiate a main chunk that the compiler produces.
 *
 * This class avoids having to use reflection (and deal with the related Exceptions) in user code.
 */
public class ChunkFactory {

  private final Class<? extends LuaFunction<Variable, ?, ?, ?, ?>> chunk;
  private final String chunkName;

  public ChunkFactory(Class<? extends LuaFunction<Variable, ?, ?, ?, ?>> chunk, String chunkName) {
    this.chunk = chunk;
    this.chunkName = chunkName;
  }

  /**
   * Creates a new instance of the compiled chunk with the given "_ENV" upvalue.
   *
   * @param env the _ENV upvalue for this instance, should not be shared between scripts.
   * @return a new script instance
   * @throws LoaderException when the script could not be instantiated.
   */
  public LuaFunction<Variable, ?, ?, ?, ?> newInstance(Variable env) throws LoaderException {
    try {
      return chunk.getConstructor(Variable.class).newInstance(env);
    } catch (RuntimeException | LinkageError | ReflectiveOperationException e) {
      throw new LoaderException(e, chunkName, 0, false);
    }
  }
}
