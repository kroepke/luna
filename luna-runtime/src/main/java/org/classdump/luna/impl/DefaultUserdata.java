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

package org.classdump.luna.impl;

import org.classdump.luna.Table;
import org.classdump.luna.Userdata;

/**
 * Default implementation of full userdata.
 */
public abstract class DefaultUserdata<T> extends Userdata<T> {

  private Table mt;
  private T userValue;

  /**
   * Constructs a new instance of this userdata with the specified initial {@code metatable}
   * and {@code userValue}.
   *
   * @param metatable initial metatable, may be {@code null}
   * @param userValue initial user value, may be {@code null}
   */
  public DefaultUserdata(Table metatable, T userValue) {
    this.mt = metatable;
    this.userValue = userValue;
  }

  @Override
  public Table getMetatable() {
    return mt;
  }

  @Override
  public Table setMetatable(Table mt) {
    Table old = this.mt;
    this.mt = mt;
    return old;
  }

  @Override
  public T getUserValue() {
    return userValue;
  }

  @Override
  public T setUserValue(T value) {
    T oldValue = userValue;
    this.userValue = value;
    return oldValue;
  }

}
