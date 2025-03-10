// This is a generated file. Not intended for manual editing.
package com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.impl;

import java.util.List;

import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherReadingClause;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherReadingWithReturn;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherReturn;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.*;

public class CypherReadingWithReturnImpl extends ASTWrapperPsiElement implements CypherReadingWithReturn {

  public CypherReadingWithReturnImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CypherVisitor visitor) {
    visitor.visitReadingWithReturn(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CypherVisitor) accept((CypherVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CypherReadingClause> getReadingClauseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CypherReadingClause.class);
  }

  @Override
  @Nullable
  public CypherReturn getReturn() {
    return findChildByClass(CypherReturn.class);
  }

}
