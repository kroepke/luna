package org.classdump.luna;

import static org.assertj.core.api.Assertions.assertThat;

import org.classdump.luna.compiler.CompilerChunkLoader;
import org.classdump.luna.env.RuntimeEnvironments;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.exec.DirectCallExecutor;
import org.classdump.luna.impl.StateContexts;
import org.classdump.luna.lib.StandardLibrary;
import org.classdump.luna.load.ChunkLoader;
import org.classdump.luna.load.LoaderException;
import org.classdump.luna.runtime.LuaFunction;
import org.junit.Test;

public class StringVsByteString {

  @Test
  public void returnStringAsTableKey()
      throws LoaderException, InterruptedException, CallPausedException, CallException {

    String program = "function a() return \"baz\" end\n" +
        "local x = {}\n" +
        "x[a()] = 42\n" +
        "return x";
    StateContext state = StateContexts.newDefaultInstance();
    Table env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);
    ChunkLoader loader = CompilerChunkLoader.of("strings");
    LuaFunction main = loader.loadTextChunk(new Variable(env), "", program);

    Object[] result = DirectCallExecutor.newExecutor().call(state, main);

    assertThat(((Table) result[0]).rawget("baz")).isEqualTo(42L);
  }

  @Test
  public void NonLocalMultiAssign()
      throws LoaderException, InterruptedException, CallPausedException, CallException {

    String program = "function a() return \"foo\", \"bar\" end\n" +
        "local x,y = a()\n" +
        "return x,y";

    StateContext state = StateContexts.newDefaultInstance();
    Table env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);

    ChunkLoader loader = CompilerChunkLoader.of("non_local_multi_assign");
    LuaFunction main = loader.loadTextChunk(new Variable(env), "", program);

    Object[] result = DirectCallExecutor.newExecutor().call(state, main);

    assertThat((result[0])).isEqualTo("foo");
    assertThat((result[1])).isEqualTo("bar");
  }
}
