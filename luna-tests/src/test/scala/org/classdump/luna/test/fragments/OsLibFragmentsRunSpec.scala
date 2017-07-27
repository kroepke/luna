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
package org.classdump.luna.test.fragments

import org.classdump.luna.test.FragmentExecTestSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OsLibFragmentsRunSpec extends FragmentExecTestSuite {

  override def bundles = Seq(OsLibFragments)

  override def expectations = Seq(OsLibFragments)

  override def contexts = Seq(Os, Full)

  override def steps = Seq(1, Int.MaxValue)

}

