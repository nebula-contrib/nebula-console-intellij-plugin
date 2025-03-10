package com.vesoft.jetbrains.plugin.graphdb.language.cypher.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.CypherLanguage;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.completion.metadata.elements.CypherElement;
import org.jetbrains.annotations.NotNull;

public final class RelationshipTypeCompletionProvider extends BaseCompletionProvider {
    public static final ElementPattern<PsiElement> PATTERN = PlatformPatterns
                   .psiElement()
                   .withLanguage(CypherLanguage.INSTANCE);

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        withCypherMetadataProvider(parameters, (metadataProvider -> metadataProvider.getRelationshipTypes().stream()
                .map(CypherElement::getLookupElement)
                .forEach(result::addElement)));
    }
}
