/*
 * Copyright (c) 2012. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.motorex.string.scan.impl;

import com.agileapes.motorex.string.exception.ImmatureEndOfDocumentException;
import com.agileapes.motorex.string.exception.MissingExpectedTokenException;
import com.agileapes.motorex.string.exception.NoParserAvailableException;
import com.agileapes.motorex.string.scan.DocumentScanner;
import com.agileapes.motorex.string.scan.SnippetParser;
import com.agileapes.motorex.string.token.Token;
import com.agileapes.motorex.string.token.impl.SimpleToken;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/7, 22:40)
 */
public class EnclosedSnippetParserTest {

    private String parse(String document, EnclosedSnippetParser parser) {
        return new PositionAwareDocumentScanner(document).parse(parser);
    }

    @Test(expectedExceptions = ImmatureEndOfDocumentException.class)
    public void testParseEmptyDocument() throws Exception {
        parse("", new EnclosedSnippetParser("(", ")"));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testEmptyOpening() throws Exception {
        parse("", new EnclosedSnippetParser("", ""));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullOpening() throws Exception {
        parse("", new EnclosedSnippetParser(null, ""));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testInvalidOpeningPairing() throws Exception {
        parse("", new EnclosedSnippetParser("(", "]}"));
    }

    @Test
    public void testParseUnenclosedWithoutAllowing() throws Exception {
        final String parsed = parse("this is a test", new EnclosedSnippetParser("(", ")"));
        Assert.assertTrue(parsed.isEmpty());
    }

    @Test
    public void testParseUnenclosedWithFallback() throws Exception {
        final SnippetParser fallback = new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                final int offset = scanner.read("\\s+").length();
                final int length = scanner.read("\\S+").length();
                return new SimpleToken(length, offset);
            }
        };
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("  hello   there");
        final String parsed = scanner.parse(new EnclosedSnippetParser("(", ")", fallback));
        Assert.assertEquals(parsed, "hello");
    }

    @Test
    public void testParseUnenclosedWithoutFallbackWithGeneralFallbackAvailable() throws Exception {
        final SnippetParser fallback = new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                final int offset = scanner.read("\\s+").length();
                final int length = scanner.read("\\S+").length();
                return new SimpleToken(length, offset);
            }
        };
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("  hello   there", fallback);
        final String parsed = scanner.parse(new EnclosedSnippetParser("(", ")", null, true, true));
        Assert.assertEquals(parsed, "hello");
    }

    @Test(expectedExceptions = NoParserAvailableException.class)
    public void testParseUnenclosedWithoutFallback() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("  hello   there");
        scanner.parse(new EnclosedSnippetParser("(", ")", null, true, true));
    }

    @Test
    public void testParseSimpleEnclosedWithoutNestingWithoutEscaping() throws Exception {
        final String parsed = parse("(hello(()))", new EnclosedSnippetParser("(", ")", null, false, false, null));
        Assert.assertEquals(parsed, "hello((");
    }

    @Test
    public void testParseSimpleEnclosedWithNestingWithoutEscaping() throws Exception {
        final String parsed = parse("(hello(()))", new EnclosedSnippetParser("(", ")", null, false, true, null));
        Assert.assertEquals(parsed, "hello(())");
    }

    @Test
    public void testEnclosedWithMultipleClosings() throws Exception {
        final String parsed = parse("(hello(]}", new EnclosedSnippetParser("((", "]}", null, false, true, null));
        Assert.assertEquals(parsed, "hello(]");
    }

    @Test
    public void testEnclosedWithNestingWithEscaping() throws Exception {
        final String parsed = parse("(hello\\())", new EnclosedSnippetParser("(", ")", null, false, true, '\\'));
        Assert.assertEquals(parsed, "hello\\(");
    }

    @Test
    public void testEnclosedWithNestingWithEscapingSupport() throws Exception {
        final String parsed = parse("(hello())", new EnclosedSnippetParser("(", ")", null, false, true, '\\'));
        Assert.assertEquals(parsed, "hello()");
    }

    @Test(expectedExceptions = MissingExpectedTokenException.class)
    public void testWithUnclosedOpening() throws Exception {
        parse("(hello", new EnclosedSnippetParser("(", ")"));
    }
}
