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

package org.classdump.luna.parser.ast.util;

import org.classdump.luna.parser.ast.SourceInfo;
import org.classdump.luna.parser.ast.SyntaxElement;

public abstract class AttributeUtils {

  private AttributeUtils() {
    // not to be instantiated
  }

  public static String sourceInfoString(SyntaxElement elem) {
    SourceInfo src = elem.attributes().get(SourceInfo.class);
    return src != null ? src.toString() : "?:?";
  }

  public static String lineString(SyntaxElement elem) {
    int line = elem.line();
    return line != 0 ? Integer.toString(line) : "?";
  }

}
