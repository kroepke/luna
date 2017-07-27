package org.classdump.luna.compiler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.classdump.luna.StateContext;
import org.classdump.luna.Table;
import org.classdump.luna.Variable;
import org.classdump.luna.env.RuntimeEnvironments;
import org.classdump.luna.exec.DirectCallExecutor;
import org.classdump.luna.impl.StateContexts;
import org.classdump.luna.lib.StandardLibrary;
import org.classdump.luna.load.ChunkFactory;
import org.classdump.luna.load.ChunkLoader;
import org.classdump.luna.runtime.LuaFunction;
import org.junit.Test;

public class CompilerChunkLoaderTest {

  @Test
  public void instancesUseDifferentEnv() throws Exception {

    String program = "local a = b; return a";
    StateContext state1 = StateContexts.newDefaultInstance();
    StateContext state2 = StateContexts.newDefaultInstance();
    ChunkLoader loader = CompilerChunkLoader.of("strings");
    ChunkFactory main = loader.compileTextChunk("", program);

    Table env1 = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state1);
    env1.rawset("b", "23");
    Table env2 = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state2);
    env2.rawset("b", "42");

    final Variable envUpVal1 = new Variable(env1);
    final Variable envUpVal2 = new Variable(env2);

    final LuaFunction<Variable, ?, ?, ?, ?> instance1 = main.newInstance(envUpVal1);
    final LuaFunction<Variable, ?, ?, ?, ?> instance2 = main.newInstance(envUpVal2);

    final Object[] call1 = DirectCallExecutor.newExecutor().call(state1, instance1);
    final Object[] call2 = DirectCallExecutor.newExecutor().call(state2, instance2);

    assertThat(call1).doesNotContainAnyElementsOf(Arrays.asList(call2));
  }

  @Test
  public void instancesUseDifferentState() throws Exception {
    // test that metatable changes are restricted to each instance
    String program = "local x = { a = b }; local mt = {__tostring = function(val) return x.a end } ; if b then setmetatable(x, mt) end; return tostring(x)";
    StateContext state1 = StateContexts.newDefaultInstance();
    StateContext state2 = StateContexts.newDefaultInstance();
    ChunkLoader loader = CompilerChunkLoader.of("strings");
    ChunkFactory main = loader.compileTextChunk("", program);

    Table env1 = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state1);
    env1.rawset("b", true);
    Table env2 = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state2);
    env2.rawset("b", false);

    final Variable envUpVal1 = new Variable(env1);
    final Variable envUpVal2 = new Variable(env2);

    final LuaFunction<Variable, ?, ?, ?, ?> instance1 = main.newInstance(envUpVal1);
    final LuaFunction<Variable, ?, ?, ?, ?> instance2 = main.newInstance(envUpVal2);

    final Object[] call1 = DirectCallExecutor.newExecutor().call(state1, instance1);
    final Object[] call2 = DirectCallExecutor.newExecutor().call(state2, instance2);

    assertThat(call1).containsExactlyInAnyOrder(true);
    assertThat(call2).matches(objects -> objects[0].toString().startsWith("table:"));
  }
}