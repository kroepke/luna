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

import org.classdump.luna.Metatables;
import org.classdump.luna.StateContext;
import org.classdump.luna.Table;
import org.classdump.luna.Userdata;
import org.classdump.luna.Variable;
import org.classdump.luna.compiler.CompilerChunkLoader;
import org.classdump.luna.env.RuntimeEnvironments;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.exec.DirectCallExecutor;
import org.classdump.luna.impl.ImmutableTable;
import org.classdump.luna.impl.NonsuspendableFunctionException;
import org.classdump.luna.impl.StateContexts;
import org.classdump.luna.lib.StandardLibrary;
import org.classdump.luna.load.ChunkLoader;
import org.classdump.luna.load.LoaderException;
import org.classdump.luna.runtime.AbstractFunctionAnyArg;
import org.classdump.luna.runtime.ExecutionContext;
import org.classdump.luna.runtime.LuaFunction;
import org.classdump.luna.runtime.ResolvedControlThrowable;

import java.util.Arrays;

public class CallableUserdata {

	abstract static class AbstractCallableObject extends Userdata<Object> {

		private static final Table mt = new ImmutableTable.Builder()
				.add(Metatables.MT_CALL, new Call())
				.build();

		@Override
		public Table getMetatable() {
			return mt;
		}

		@Override
		public Table setMetatable(Table mt) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getUserValue() {
			return null;
		}

		@Override
		public Object setUserValue(Object value) {
			throw new UnsupportedOperationException();
		}

		protected abstract Object[] call(Object[] args);

		private static class Call extends AbstractFunctionAnyArg {

			@Override
			public void invoke(ExecutionContext context, Object[] args) throws ResolvedControlThrowable {
				AbstractCallableObject o = (AbstractCallableObject) args[0];
				Object[] result = o.call(Arrays.copyOfRange(args, 1, args.length));
				context.getReturnBuffer().setToContentsOf(result);
			}

			@Override
			public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
				throw new NonsuspendableFunctionException();
			}

		}

	}

	static class ExampleCallableObject extends AbstractCallableObject {

		@Override
		protected Object[] call(Object[] args) {

			// reverse args

			for (int i = 0; i < args.length / 2; i++) {
				int j = args.length - i - 1;

				Object tmp = args[i];
				args[i] = args[j];
				args[j] = tmp;
			}

			return args;
		}

	}

	public static void main(String[] args)
			throws InterruptedException, CallPausedException, CallException, LoaderException {

		// initialise state
		StateContext state = StateContexts.newDefaultInstance();

		// load the standard library; env is the global environment
		Table env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);
		env.rawset("object", new ExampleCallableObject());

		// the main function as Lua source
		String program =
				"print(object)\n"
				+ "print(type(object))\n"
				+ "return object('hello', 'world', 1, 2.0, nil)";

		// load the main function
		ChunkLoader loader = CompilerChunkLoader.of("example");
		LuaFunction main = loader.loadTextChunk(new Variable(env), "example", program);

		// run the main function
		Object[] result = DirectCallExecutor.newExecutor().call(state, main);

		// prints [null, 2.0, 1, world, hello]
		System.out.println("Result: " + Arrays.toString(result));

	}

}
