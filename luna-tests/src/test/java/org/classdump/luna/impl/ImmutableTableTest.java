/*
 * Copyright 2017 Kay Roepke
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import org.junit.Test;

public class ImmutableTableTest {

  @Test
  public void bothEmpty() {
    ImmutableTable first = new ImmutableTable.Builder().build();
    ImmutableTable second = new ImmutableTable.Builder().build();

    assertTrue(first.equals(second));
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  public void emptyTableHashcodeWorks() {
    try {
      new HashMap<>().put(new ImmutableTable.Builder().build(), "foo");
    } catch (Exception ex) {
      fail("Should not throw exception");
    }
  }
}