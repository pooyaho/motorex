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

import com.agileapes.motorex.string.exception.IllegalScannerSnapshotException;
import com.agileapes.motorex.string.exception.ImmatureEndOfDocumentException;
import com.agileapes.motorex.string.exception.MissingExpectedTokenException;
import com.agileapes.motorex.string.scan.DocumentScanner;
import com.agileapes.motorex.string.scan.ScannerSnapshot;
import com.agileapes.motorex.string.scan.SnippetParser;
import com.agileapes.motorex.string.token.Token;
import com.agileapes.motorex.string.token.impl.SimpleToken;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/7, 20:16)
 */
public class PositionAwareDocumentScannerTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullDocument() throws Exception {
        new PositionAwareDocumentScanner(null);
    }

    @Test
    /**
     * This simple test examines the scanner implementation's concrete behaviour for
     * peeking, reading, and cursor positioning, when the scanner is simply moving
     * forward through the document
     */
    public void testTextFlow() throws Exception {
        final String document = "This is a test";
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        Assert.assertEquals(scanner.getDocument(), document);
        Assert.assertEquals(scanner.length(), document.length());
        Assert.assertEquals(0, scanner.getOffset());
        char peek = scanner.peek();
        for (int i = 0; i < document.length(); i ++) {
            Assert.assertEquals(scanner.getRemainder(), document.substring(i));
            Assert.assertEquals(scanner.peek(scanner.remaining()), document.substring(i));
            final char read = scanner.read();
            Assert.assertEquals(read, peek);
            try {
                peek = scanner.peek();
            } catch (Error e) {
                //When we have reached the end of the document, peeking
                //is expected to generate an error
                Assert.assertEquals(i, document.length() - 1);
                break;
            }
            if (i == document.length() - 1) {
                //If no errors have been raised, then the scanner is misbehaving
                throw new Exception();
            }
        }
        Assert.assertEquals(scanner.remaining(), 0);
        Assert.assertEquals(scanner.getOffset(), document.length());
    }

    @Test
    /**
     * This test will simply examine the normal behaviour expected of the scanner's implementation
     * regarding remembering snapshots, resetting to them in a stack-based manner, and then letting
     * them go, while using restore to carry on.
     */
    public void testRememberingResettingAndForgetting() throws Exception {
        final String document = "abc";
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < document.length(); i ++) {
            //we remember the setting, right before reading anything
            scanner.remember();
            //this variable holds the position regarding the right side of the text before reading
            final int beforeReading = scanner.remaining();
            Assert.assertEquals(beforeReading, document.length() - i);
            final char read = scanner.read();
            Assert.assertEquals(read, document.charAt(i));
            //after reading, the position should change by one
            Assert.assertEquals(scanner.remaining(), beforeReading - 1);
            //we remember this, so that we can continue on with the reading without reading the same character
            //again and again
            final ScannerSnapshot snapshot = scanner.remember();
            //we forget the snapshot we just set
            scanner.forget();
            //we reset to the point before reading the character
            scanner.reset();
            Assert.assertEquals(scanner.remaining(), beforeReading);
            //and now we forget that old position ...
            scanner.forget();
            //... instead going back to reading the rest of the text
            scanner.restore(snapshot);
        }
    }

    @Test(expectedExceptions = ImmatureEndOfDocumentException.class)
    public void testPeekChar() throws Exception {
        final String document = "this is a test";
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < document.length(); i ++) {
            char c = document.charAt(i);
            final char peek = scanner.peek();
            Assert.assertEquals(peek, c);
            Assert.assertEquals(scanner.read(), peek);
        }
        scanner.peek();
    }

    @Test(expectedExceptions = ImmatureEndOfDocumentException.class)
    public void testPeekString() throws Exception {
        final String document = "This is a test";
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        final String peek = scanner.peek(document.length());
        Assert.assertEquals(peek, document);
        Assert.assertEquals(scanner.read(document.length()), peek);
        scanner.peek(document.length());
    }

    @Test(expectedExceptions = ImmatureEndOfDocumentException.class)
    public void testReadString() throws Exception {
        final String prototype = "123";
        String document = "";
        final int count = 10;
        for (int i = 0; i < count; i ++) {
            document += prototype;
        }
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < count; i ++) {
            Assert.assertEquals(scanner.read(prototype.length()), prototype);
        }
        Assert.assertEquals(scanner.remaining(), 0);
        scanner.read(1);
    }

    @Test
    public void testReadingByPattern() throws Exception {
        final String prototype = "123abc";
        String document = "";
        final int count = 10;
        final String pattern = "(\\d+|[a-z]+)";
        for (int i = 0; i < count; i ++) {
            document += prototype;
        }
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < count; i ++) {
            Assert.assertEquals(scanner.read(pattern), "123");
            Assert.assertEquals(scanner.read(pattern), "abc");
        }
        Assert.assertEquals(scanner.remaining(), 0);
        scanner.read(pattern);
    }

    @Test
    public void testReadUntil() throws Exception {
        final String document = "sin(90)";
        final String[] delimiters = {"(", ")"};
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        Assert.assertEquals(scanner.readUntil(delimiters), "sin");
        Assert.assertEquals(scanner.read(), '(');
        Assert.assertEquals(scanner.readUntil(delimiters), "90");
        Assert.assertEquals(scanner.read(), ')');
        Assert.assertEquals(scanner.readUntil(delimiters), "");
        Assert.assertEquals(scanner.remaining(), 0);
    }

    @Test(expectedExceptions = MissingExpectedTokenException.class)
    public void testExpectCharacter() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("12");
        Assert.assertEquals(scanner.expect('1'), '1');
        scanner.expect('1');
    }

    @Test(expectedExceptions = MissingExpectedTokenException.class)
    public void testExpectTokens() throws Exception {
        final String document = "abcd";
        final String[] expected = {"a", "b", "c"};
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < document.length() - 1; i ++) {
            Assert.assertEquals(scanner.expect(expected), document.substring(i, i + 1));
        }
        Assert.assertEquals(scanner.getRemainder(), document.substring(document.length() - 1));
        scanner.expect(expected);
    }

    @Test(expectedExceptions = MissingExpectedTokenException.class)
    public void testExpectPatterns() throws Exception {
        final Pattern[] patterns = {Pattern.compile("[a-z]"), Pattern.compile("\\d+")};
        final String document = "a1b2c+";
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < document.length() - 1; i ++) {
            Assert.assertEquals(scanner.expect(patterns), document.substring(i, i + 1));
        }
        Assert.assertEquals(scanner.getRemainder(), document.substring(document.length() - 1));
        scanner.expect(patterns);
    }

    @Test
    public void testHas() throws Exception {
        final String prototype = "abc";
        String document = "";
        final int count = 10;
        for (int i = 0; i < count; i ++) {
            document += prototype;
        }
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < count; i ++) {
            final int remaining = scanner.remaining();
            Assert.assertTrue(remaining > 0);
            Assert.assertTrue(scanner.has(prototype));
            Assert.assertEquals(scanner.remaining(), remaining);
            Assert.assertFalse(scanner.has("123"));
            Assert.assertEquals(scanner.remaining(), remaining);
            Assert.assertEquals(scanner.read(prototype.length()), prototype);
        }
        Assert.assertEquals(scanner.remaining(), 0);
        Assert.assertFalse(scanner.has(prototype));
    }

    @Test
    public void testMatches() throws Exception {
        final String prototype = "a1";
        final int count = 10;
        final String pattern = "[a-z]+";
        String document = "";
        for (int i = 0; i < count; i ++) {
            document += prototype;
        }
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        for (int i = 0; i < count; i ++) {
            final int remaining = scanner.remaining();
            Assert.assertTrue(remaining > 0);
            Assert.assertTrue(scanner.matches(pattern));
            Assert.assertEquals(scanner.remaining(), remaining);
            Assert.assertEquals(scanner.read(), 'a');
            Assert.assertFalse(scanner.matches(pattern));
            Assert.assertEquals(scanner.read(), '1');
        }
    }

    @Test(expectedExceptions = IllegalScannerSnapshotException.class)
    public void testIllegalSnapshotType() throws Exception {
        new PositionAwareDocumentScanner("").restore(new SimpleScannerSnapshot("", 0));
    }

    @Test(expectedExceptions = IllegalScannerSnapshotException.class)
    public void testOverreachingRestore() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("");
        scanner.restore(new PositionAwareScannerSnapshot("", 3, 1, 1));
    }

    @Test(expectedExceptions = IllegalScannerSnapshotException.class)
    public void testRestorationOfNullDocument() throws Exception {
        new PositionAwareDocumentScanner("").restore(new PositionAwareScannerSnapshot(null, 0, 0, 0));
    }

    @Test
    public void testPush() throws Exception {
        final String document = "this is a test";
        final String modified = "this is not a test";
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner(document);
        Assert.assertEquals(scanner.getRemainder(), document);
        Assert.assertEquals(scanner.read("this\\s+is\\s+"), "this is ");
        scanner.push("not ");
        Assert.assertTrue(scanner.has("not"));
        scanner.rewind();
        Assert.assertEquals(scanner.read(scanner.remaining()), modified);
    }

    @Test
    public void testNoOpParser() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("test");
        final String parsed = scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                return new SimpleToken(0);
            }
        });
        Assert.assertEquals(parsed, "");
    }

    @Test
    public void testFailingParser() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("test");
        final String parsed = scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                return null;
            }
        });
        Assert.assertEquals(parsed, "");
    }

    @Test
    public void testNoOpParserWithMemory() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("test");
        final String parsed = scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                scanner.read();
                scanner.remember();
                scanner.forget();
                return null;
            }
        });
        Assert.assertEquals(parsed, "");
    }

    @Test
    public void testParserWithMemory() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("this is a test");
        final String parsed = scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                scanner.remember();
                final String skipped = scanner.read("\\S+\\s+");
                scanner.remember();
                final String read = scanner.read("\\S+");
                //jumping back to right before "is"
                scanner.reset();
                scanner.forget();
                //jumping back to right before "this "
                scanner.reset();
                scanner.forget();
                return new SimpleToken(read.length(), skipped.length());
            }
        });
        Assert.assertEquals(parsed, "is");
        Assert.assertEquals(scanner.getRemainder(), " a test");
    }

    @Test
    public void testSloppyParser() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("  this is a test");
        final String parsed = scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                scanner.remember();
                final int skipped = scanner.read("\\s*").length();
                final int length = scanner.read("[a-z]+").length();
                return new SimpleToken(length, skipped);
            }
        });
        Assert.assertEquals(parsed, "this");
        Assert.assertEquals(scanner.getRemainder(), " is a test");
    }

    @Test(expectedExceptions = IllegalScannerSnapshotException.class)
    public void testIllegalMemorizationInParser() throws Exception {
        final PositionAwareDocumentScanner scanner = new PositionAwareDocumentScanner("test");
        scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                scanner.forget();
                return null;
            }
        });
    }
}
