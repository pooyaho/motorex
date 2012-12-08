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

package com.agileapes.motorex.tree.impl;

import com.agileapes.motorex.string.scan.DocumentScanner;
import com.agileapes.motorex.string.scan.SnippetParser;
import com.agileapes.motorex.string.scan.impl.EnclosedSnippetParser;
import com.agileapes.motorex.string.scan.impl.PositionAwareDocumentScanner;
import com.agileapes.motorex.string.token.Token;
import com.agileapes.motorex.string.token.impl.SimpleToken;
import com.agileapes.motorex.tree.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 0:18)
 */
public abstract class AbstractSearchableNode implements Node {

    public static final String ABSOLUTE_QUERY_DESIGNATOR = "/";
    public static final String QUERY_SECTION_SEPARATOR = "/";
    public static final String STANDARD_QUOTATION = "'\"`";

    private static final class QueryReader implements SnippetParser {

        private boolean absolute;

        @Override
        public Token parse(DocumentScanner scanner) {
            //reading absolute designator
            int offset = 0;
            absolute = scanner.has(ABSOLUTE_QUERY_DESIGNATOR);
            if (absolute) {
                offset ++;
                scanner.read();
            }
            //skipping whitespace
            offset += scanner.read("\\s+").length();
            String query = "";
            while (scanner.remaining() > 0) {
                final String next = scanner.peek(1);
                if (STANDARD_QUOTATION.contains(next)) {
                    query += next;
                    query += scanner.parse(new EnclosedSnippetParser(next, next, null, false, true, '\\'));
                    query += scanner.expect(next.charAt(0));
                    continue;
                } else if (next.equals(QUERY_SECTION_SEPARATOR)) {
                    //this is the end of the query, unless it has been escaped.
                    if (query.isEmpty() || !query.endsWith("\\")) {
                        break;
                    }
                }
                query += scanner.read();
            }
            return new SimpleToken(query.length(), offset);
        }

        public boolean isAbsolute() {
            return absolute;
        }
    }

    @Override
    public List<Node> find(String query) {
        final DocumentScanner scanner = new PositionAwareDocumentScanner(query);
        final QueryReader queryReader = new QueryReader();
        final String description = scanner.parse(queryReader);
        final String remainder = scanner.getRemainder();
        final ArrayList<Node> agenda = new ArrayList<Node>();
        if (queryReader.isAbsolute()) {
            if (!this.matches(description)) {
                return Collections.emptyList();
            }
            agenda.add(this);
        } else {
            if (this.matches(description)) {
                agenda.add(this);
            }
            for (Node node : this.getChildren()) {
                agenda.addAll(node.find(description));
            }
        }
        if (remainder.isEmpty()) {
            return agenda;
        }
        scanner.expect(QUERY_SECTION_SEPARATOR);
        final ArrayList<Node> result = new ArrayList<Node>();
        for (Node node : agenda) {
            for (Node child : node.getChildren()) {
                final List<Node> exploration = child.find(remainder);
                result.addAll(exploration);
            }
        }
        return result;
    }

}
