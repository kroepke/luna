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

package org.classdump.luna.compiler.util;

import org.classdump.luna.compiler.ir.BinOp;
import org.classdump.luna.compiler.ir.Branch;
import org.classdump.luna.compiler.ir.CPUWithdraw;
import org.classdump.luna.compiler.ir.Call;
import org.classdump.luna.compiler.ir.Closure;
import org.classdump.luna.compiler.ir.IRNode;
import org.classdump.luna.compiler.ir.IRVisitor;
import org.classdump.luna.compiler.ir.Jmp;
import org.classdump.luna.compiler.ir.LoadConst;
import org.classdump.luna.compiler.ir.MultiGet;
import org.classdump.luna.compiler.ir.PhiLoad;
import org.classdump.luna.compiler.ir.PhiStore;
import org.classdump.luna.compiler.ir.Ret;
import org.classdump.luna.compiler.ir.TCall;
import org.classdump.luna.compiler.ir.TabGet;
import org.classdump.luna.compiler.ir.TabNew;
import org.classdump.luna.compiler.ir.TabRawAppendMulti;
import org.classdump.luna.compiler.ir.TabRawSet;
import org.classdump.luna.compiler.ir.TabRawSetInt;
import org.classdump.luna.compiler.ir.TabSet;
import org.classdump.luna.compiler.ir.ToNext;
import org.classdump.luna.compiler.ir.ToNumber;
import org.classdump.luna.compiler.ir.UnOp;
import org.classdump.luna.compiler.ir.UpLoad;
import org.classdump.luna.compiler.ir.UpStore;
import org.classdump.luna.compiler.ir.VarInit;
import org.classdump.luna.compiler.ir.VarLoad;
import org.classdump.luna.compiler.ir.VarStore;
import org.classdump.luna.compiler.ir.Vararg;

public abstract class DefaultNodeActionVisitor extends IRVisitor {

  protected abstract void action(IRNode node);

  @Override
  public void visit(LoadConst.Nil node) {
    action(node);
  }

  @Override
  public void visit(LoadConst.Bool node) {
    action(node);
  }

  @Override
  public void visit(LoadConst.Int node) {
    action(node);
  }

  @Override
  public void visit(LoadConst.Flt node) {
    action(node);
  }

  @Override
  public void visit(LoadConst.Str node) {
    action(node);
  }

  @Override
  public void visit(BinOp node) {
    action(node);
  }

  @Override
  public void visit(UnOp node) {
    action(node);
  }

  @Override
  public void visit(TabNew node) {
    action(node);
  }

  @Override
  public void visit(TabGet node) {
    action(node);
  }

  @Override
  public void visit(TabSet node) {
    action(node);
  }

  @Override
  public void visit(TabRawSet node) {
    action(node);
  }

  @Override
  public void visit(TabRawSetInt node) {
    action(node);
  }

  @Override
  public void visit(TabRawAppendMulti node) {
    action(node);
  }

  @Override
  public void visit(VarInit node) {
    action(node);
  }

  @Override
  public void visit(VarLoad node) {
    action(node);
  }

  @Override
  public void visit(VarStore node) {
    action(node);
  }

  @Override
  public void visit(UpLoad node) {
    action(node);
  }

  @Override
  public void visit(UpStore node) {
    action(node);
  }

  @Override
  public void visit(Vararg node) {
    action(node);
  }

  @Override
  public void visit(Ret node) {
    action(node);
  }

  @Override
  public void visit(TCall node) {
    action(node);
  }

  @Override
  public void visit(Call node) {
    action(node);
  }

  @Override
  public void visit(MultiGet node) {
    action(node);
  }

  @Override
  public void visit(PhiStore node) {
    action(node);
  }

  @Override
  public void visit(PhiLoad node) {
    action(node);
  }

  @Override
  public void visit(Jmp node) {
    action(node);
  }

  @Override
  public void visit(Closure node) {
    action(node);
  }

  @Override
  public void visit(ToNumber node) {
    action(node);
  }

  @Override
  public void visit(ToNext node) {
    action(node);
  }

  @Override
  public void visit(Branch node) {
    action(node);
  }

  @Override
  public void visit(CPUWithdraw node) {
    action(node);
  }

}
