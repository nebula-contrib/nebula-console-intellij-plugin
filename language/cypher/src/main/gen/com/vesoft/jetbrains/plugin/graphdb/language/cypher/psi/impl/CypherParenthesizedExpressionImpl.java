// This is a generated file. Not intended for manual editing.
package com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.impl;

import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherExpression;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherParenthesizedExpression;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.*;

public class CypherParenthesizedExpressionImpl extends ASTWrapperPsiElement implements CypherParenthesizedExpression {

  public CypherParenthesizedExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CypherVisitor visitor) {
    visitor.visitParenthesizedExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CypherVisitor) accept((CypherVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public CypherExpression getExpression() {
    return findChildByClass(CypherExpression.class);
  }

}
