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

package org.classdump.luna.runtime;

/**
 * Abstract function of five arguments.
 */
public abstract class AbstractFunction5<T1, T2, T3, T4, T5> extends
    LuaFunction<T1, T2, T3, T4, T5> {

  @Override
  public void invoke(ExecutionContext context) throws ResolvedControlThrowable {
    invoke(context, null, null, null, null, null);
  }

  @Override
  public void invoke(ExecutionContext context, T1 arg1) throws ResolvedControlThrowable {
    invoke(context, arg1, null, null, null, null);
  }

  @Override
  public void invoke(ExecutionContext context, T1 arg1, T2 arg2) throws ResolvedControlThrowable {
    invoke(context, arg1, arg2, null, null, null);
  }

  @Override
  public void invoke(ExecutionContext context, T1 arg1, T2 arg2, T3 arg3)
      throws ResolvedControlThrowable {
    invoke(context, arg1, arg2, arg3, null, null);
  }

  @Override
  public void invoke(ExecutionContext context, T1 arg1, T2 arg2, T3 arg3, T4 arg4)
      throws ResolvedControlThrowable {
    invoke(context, arg1, arg2, arg3, arg4, null);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void invoke(ExecutionContext context, Object[] args) throws ResolvedControlThrowable {
    T1 a = null;
    T2 b = null;
    T3 c = null;
    T4 d = null;
    T5 e = null;
    switch (args.length) {
      default:
      case 5:
        e = (T5) args[4]; // fall through
      case 4:
        d = (T4) args[3]; // fall through
      case 3:
        c = (T3) args[2]; // fall through
      case 2:
        b = (T2) args[1]; // fall through
      case 1:
        a = (T1) args[0]; // fall through
      case 0:
    }
    invoke(context, a, b, c, d, e);
  }

}
