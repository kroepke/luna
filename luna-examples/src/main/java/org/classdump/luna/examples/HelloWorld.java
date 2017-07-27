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

import org.classdump.luna.StateContext;
import org.classdump.luna.Table;
import org.classdump.luna.Variable;
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

public class HelloWorld {

  public static void main(String[] args)
      throws InterruptedException, CallPausedException, CallException, LoaderException {

    String program = "print('hello world!')";

    StateContext state = StateContexts.newDefaultInstance();
    Table env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);

    ChunkLoader loader = CompilerChunkLoader.of("hello_world");
    LuaFunction main = loader.loadTextChunk(new Variable(env), "hello", program);

    DirectCallExecutor.newExecutor().call(state, main);
  }

}
