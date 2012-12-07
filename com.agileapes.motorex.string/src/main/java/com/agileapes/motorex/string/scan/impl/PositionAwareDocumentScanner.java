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
import com.agileapes.motorex.string.text.PatternFactory;
import com.agileapes.motorex.string.text.PositionAwareTextHandler;
import com.agileapes.motorex.string.text.impl.DefaultPatternFactory;
import com.agileapes.motorex.string.text.impl.SimplePositionHandler;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/7, 19:38)
 */
public class PositionAwareDocumentScanner implements DocumentScanner, PositionAwareTextHandler {

    private final SimplePositionHandler positionHandler;
    private String document;
    private PatternFactory patternFactory;
    private int cursor;
    private Stack<PositionAwareScannerSnapshot> marks;

    public PositionAwareDocumentScanner(String document) {
        this(document, new DefaultPatternFactory(Pattern.DOTALL | Pattern.MULTILINE));
    }

    public PositionAwareDocumentScanner(String document, PatternFactory patternFactory) {
        this.document = document;
        this.patternFactory = patternFactory;
        this.positionHandler = new SimplePositionHandler();
        this.marks = new Stack<PositionAwareScannerSnapshot>();
        reset();
    }

    @Override
    public String getDocument() {
        return document;
    }

    @Override
    public int getOffset() {
        return cursor;
    }

    @Override
    public char peek() {
        if (remaining() == 0) {
            throw new ImmatureEndOfDocumentException();
        }
        return document.charAt(cursor);
    }

    @Override
    public String peek(int length) {
        if (remaining() < length) {
            throw new ImmatureEndOfDocumentException();
        }
        return document.substring(cursor, cursor + length);
    }

    @Override
    public char read() {
        final char peek = peek();
        cursor ++;
        positionHandler.readChar(peek);
        return peek;
    }

    @Override
    public String read(int length) {
        final String peek = peek(length);
        cursor += length;
        positionHandler.readString(peek);
        return peek;
    }

    @Override
    public String read(String pattern) {
        return read(patternFactory.getPattern(pattern));
    }

    @Override
    public String read(Pattern pattern) {
        final Matcher matcher = pattern.matcher(document.substring(cursor));
        if (matcher.find() && matcher.start() == 0) {
            final String read = matcher.group();
            cursor += read.length();
            positionHandler.readString(read);
            return read;
        }
        return "";
    }

    @Override
    public String readUntil(String... delimiters) {
        String read = "";
        while (remaining() > 0) {
            boolean done = false;
            for (String delimiter : delimiters) {
                if (has(delimiter)) {
                    done = true;
                    break;
                }
            }
            if (done) {
                break;
            }
            read += read();
        }
        return read;
    }

    @Override
    public char expect(char c) throws MissingExpectedTokenException {
        final char read = read();
        if (read != c) {
            throw new MissingExpectedTokenException();
        }
        return read;
    }

    @Override
    public String expect(String... tokens) throws MissingExpectedTokenException {
        for (String token : tokens) {
            if (has(token)) {
                return read(token);
            }
        }
        throw new MissingExpectedTokenException();
    }

    @Override
    public String expect(Pattern... patterns) throws MissingExpectedTokenException {
        for (Pattern pattern : patterns) {
            if (matches(pattern)) {
                return read(pattern);
            }
        }
        throw new MissingExpectedTokenException();
    }

    @Override
    public boolean has(String token) {
        if (token.length() > remaining()) {
            throw new ImmatureEndOfDocumentException();
        }
        return document.substring(cursor).startsWith(token);
    }

    @Override
    public boolean matches(String pattern) {
        return matches(patternFactory.getPattern(pattern));
    }

    @Override
    public boolean matches(Pattern pattern) {
        final Matcher matcher = pattern.matcher(document.substring(cursor));
        return matcher.find() && matcher.start() == 0;
    }

    @Override
    public int length() {
        return document.length();
    }

    @Override
    public int remaining() {
        return document.length() - cursor;
    }

    @Override
    public void push(String text) {
        document = document.substring(0, cursor) + text + document.substring(cursor);
    }

    @Override
    public void reset() {
        restore(marks.peek());
    }

    @Override
    public ScannerSnapshot remember() {
        final PositionAwareScannerSnapshot snapshot = new PositionAwareScannerSnapshot(document, cursor, getLine(), getColumn());
        marks.push(snapshot);
        return snapshot;
    }

    @Override
    public void forget() {
        marks.pop();
    }

    @Override
    public void restore(ScannerSnapshot snapshot) {
        if (!(snapshot instanceof PositionAwareTextHandler)) {
            throw new IllegalScannerSnapshotException();
        }
        this.document = snapshot.getDocument();
        this.cursor = snapshot.getOffset();
        this.positionHandler.restore(((PositionAwareTextHandler) snapshot).getLine(), ((PositionAwareTextHandler) snapshot).getColumn());
    }

    @Override
    public String parse(SnippetParser parser) {
        final ScannerSnapshot snapshot = remember();
        final int marked = marks.size();
        final String parsedText = parser.parse(this);
        if (marked != marks.size()) {
            throw new IllegalScannerSnapshotException();
        }
        restore(snapshot);
        forget();
        return parsedText;
    }

    @Override
    public int getLine() {
        return positionHandler.getLine();
    }

    @Override
    public int getColumn() {
        return positionHandler.getColumn();
    }

}
