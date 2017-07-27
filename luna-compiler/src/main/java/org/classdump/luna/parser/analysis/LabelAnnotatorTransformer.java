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

package org.classdump.luna.parser.analysis;

import org.classdump.luna.parser.ast.BodyStatement;
import org.classdump.luna.parser.ast.Expr;
import org.classdump.luna.parser.ast.FunctionDefExpr;
import org.classdump.luna.parser.ast.GotoStatement;
import org.classdump.luna.parser.ast.LabelStatement;
import org.classdump.luna.parser.ast.Transformer;

public abstract class LabelAnnotatorTransformer extends Transformer {

  protected abstract Object annotation(LabelStatement node);

  protected abstract Object annotation(GotoStatement node);

  @Override
  public BodyStatement transform(LabelStatement node) {
    return node.with(annotation(node));
  }

  @Override
  public BodyStatement transform(GotoStatement node) {
    return node.with(annotation(node));
  }

  @Override
  public Expr transform(FunctionDefExpr e) {
    // don't descend into function literals
    return e;
  }

}
