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
import org.classdump.luna.env.RuntimeEnvironments;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.exec.DirectCallExecutor;
import org.classdump.luna.impl.StateContexts;
import org.classdump.luna.lib.StandardLibrary;
import org.classdump.luna.load.LoaderException;
import org.classdump.luna.runtime.AbstractUntypedFunction3;
import org.classdump.luna.runtime.Dispatch;
import org.classdump.luna.runtime.ExecutionContext;
import org.classdump.luna.runtime.ResolvedControlThrowable;
import org.classdump.luna.runtime.UnresolvedControlThrowable;

public class TailCall {

	// equivalent to:
	//   function (a, b, c) return a(b + c) end
	static class ExampleFunction extends AbstractUntypedFunction3 {

		@Override
		public void invoke(ExecutionContext context, Object arg1, Object arg2, Object arg3) throws ResolvedControlThrowable {
			try {
				// b + c
				Dispatch.add(context, arg2, arg3);
			}
			catch (UnresolvedControlThrowable ct) {
				throw ct.resolve(this, arg1);
			}

			resume(context, arg1);
		}

		@Override
		public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
			Object additionResult = context.getReturnBuffer().get0();

			// a(b + c)
			context.getReturnBuffer().setToCall(suspendedState, additionResult);
		}

	}

	public static void main(String[] args)
			throws InterruptedException, CallPausedException, CallException, LoaderException {

		StateContext state = StateContexts.newDefaultInstance();
		Table env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);

		// prints 123.4
		DirectCallExecutor.newExecutor().call(state, new ExampleFunction(), env.rawget("print"), "100", 23.4);
	}

}
