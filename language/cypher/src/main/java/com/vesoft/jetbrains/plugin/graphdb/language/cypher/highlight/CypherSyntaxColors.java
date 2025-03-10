package com.vesoft.jetbrains.plugin.graphdb.language.cypher.highlight;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * TODO: Description
 *
 * @author dmitry@vrublesvky.me
 */
public class CypherSyntaxColors {

    public static final TextAttributesKey LINE_COMMENT =
            createTextAttributesKey("CYPHER_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            createTextAttributesKey("CYPHER_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("CYPHER_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey VARIABLE =
            createTextAttributesKey("CYPHER_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    public static final TextAttributesKey FUNCTION =
            createTextAttributesKey("CYPHER_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey LABEL =
            createTextAttributesKey("CYPHER_LABEL", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey RELATIONSHIP_TYPE =
            createTextAttributesKey("CYPHER_RELATIONSHIP_TYPE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey PARAMETER =
            createTextAttributesKey("CYPHER_PARAMETER", DefaultLanguageHighlighterColors.INSTANCE_FIELD);

    public static final TextAttributesKey SEMICOLON =
            createTextAttributesKey("CYPHER_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey OPERATION_SIGN =
            createTextAttributesKey("CYPHER_OPERATION", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey PARENTHESES =
            createTextAttributesKey("CYPHER_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey CURLY_BRACES =
            createTextAttributesKey("CYPHER_CURLY_BRACES", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey SQUARE_BRACES =
            createTextAttributesKey("CYPHER_SQUARE_BRACES", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey COMMA =
            createTextAttributesKey("CYPHER_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey DOT =
            createTextAttributesKey("CYPHER_DOT", DefaultLanguageHighlighterColors.DOT);

    public static final TextAttributesKey STRING =
            createTextAttributesKey("CYPHER_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("CYPHER_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
}
