// This is a generated file. Not intended for manual editing.
package com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.impl;

import java.util.List;

import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherIntegerLiteral;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherRangeLiteral;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.CypherVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.psi.*;

public class CypherRangeLiteralImpl extends ASTWrapperPsiElement implements CypherRangeLiteral {

  public CypherRangeLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CypherVisitor visitor) {
    visitor.visitRangeLiteral(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CypherVisitor) accept((CypherVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CypherIntegerLiteral> getIntegerLiteralList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CypherIntegerLiteral.class);
  }

}
