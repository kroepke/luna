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
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.exec.DirectCallExecutor;
import org.classdump.luna.impl.NonsuspendableFunctionException;
import org.classdump.luna.impl.StateContexts;
import org.classdump.luna.load.LoaderException;
import org.classdump.luna.runtime.AbstractFunction0;
import org.classdump.luna.runtime.ExecutionContext;
import org.classdump.luna.runtime.ResolvedControlThrowable;

public class SimpleJavaFunction {

  public static void main(String[] args)
      throws InterruptedException, CallPausedException, CallException, LoaderException {

    StateContext state = StateContexts.newDefaultInstance();
    Object[] result = DirectCallExecutor.newExecutor().call(state, new ExampleFunction());

    System.out.println("Result: " + Arrays.toString(result));

  }

  static class ExampleFunction extends AbstractFunction0 {

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
