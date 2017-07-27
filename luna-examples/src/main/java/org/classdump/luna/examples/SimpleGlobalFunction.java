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

package org.classdump.luna.examples;

import java.util.Arrays;
import org.classdump.luna.StateContext;
import org.classdump.luna.Table;
import org.classdump.luna.Variable;
import org.classdump.luna.compiler.CompilerChunkLoader;
import org.classdump.luna.env.RuntimeEnvironments;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.exec.DirectCallExecutor;
import org.classdump.luna.impl.NonsuspendableFunctionException;
import org.classdump.luna.impl.StateContexts;
import org.classdump.luna.lib.StandardLibrary;
import org.classdump.luna.load.ChunkLoader;
import org.classdump.luna.load.LoaderException;
import org.classdump.luna.runtime.AbstractFunction0;
import org.classdump.luna.runtime.ExecutionContext;
import org.classdump.luna.runtime.LuaFunction;
import org.classdump.luna.runtime.ResolvedControlThrowable;

public class SimpleGlobalFunction {

  public static void main(String[] args)
      throws InterruptedException, CallPausedException, CallException, LoaderException {

    // initialise state
    StateContext state = StateContexts.newDefaultInstance();

    // load the standard library; env is the global environment
    Table env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);
    env.rawset("now", new Now());

    // load the main function
    ChunkLoader loader = CompilerChunkLoader.of("example");
    LuaFunction main = loader.loadTextChunk(new Variable(env), "example", "return now()");

    // run the main function
    Object[] result = DirectCallExecutor.newExecutor().call(state, main);

    // prints the number of milliseconds since 1 Jan 1970
    System.out.println("Result: " + Arrays.toString(result));

  }

  // A simple function that returns the result of System.currentTimeMillis()
  static class Now extends AbstractFunction0 {

    @Override
    public void invoke(ExecutionContext context) throws ResolvedControlThrowable {
      context.getReturnBuffer().setTo(System.currentTimeMillis());
    }

    @Override
    public void resume(ExecutionContext context, Object suspendedState)
        throws ResolvedControlThrowable {
      throw new NonsuspendableFunctionException();
    }

  }

}
