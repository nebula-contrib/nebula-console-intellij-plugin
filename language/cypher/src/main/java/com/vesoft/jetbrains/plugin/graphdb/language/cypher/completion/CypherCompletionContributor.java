package com.vesoft.jetbrains.plugin.graphdb.language.cypher.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.completion.providers.*;
import com.vesoft.jetbrains.plugin.graphdb.language.cypher.completion.providers.*;

/**
 * Entry point for Cypher auto completion.
 */
public class CypherCompletionContributor extends CompletionContributor {

    public CypherCompletionContributor() {
        extend(CompletionType.BASIC,
                KeywordCompletionProvider.PATTERN,
                new KeywordCompletionProvider());

        extend(CompletionType.BASIC,
                BuiltInFunctionCompletionProvider.PATTERN,
                new BuiltInFunctionCompletionProvider());

        extend(CompletionType.BASIC,
                LabelsCompletionProvider.PATTERN,
                new LabelsCompletionProvider());

        extend(CompletionType.BASIC,
                RelationshipTypeCompletionProvider.PATTERN,
                new RelationshipTypeCompletionProvider());

        extend(CompletionType.BASIC,
                PropertyKeyCompletionProvider.PATTERN,
                new PropertyKeyCompletionProvider());

        extend(CompletionType.BASIC,
                ProceduresCompletionProvider.PATTERN,
                new ProceduresCompletionProvider());

        extend(CompletionType.BASIC,
                UserFunctionsCompletionProvider.PATTERN,
                new UserFunctionsCompletionProvider());

        extend(CompletionType.BASIC,
                NebulaCompletionProvider.PATTERN,
                new NebulaCompletionProvider());
    }
}
