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

package com.agileapes.motorex.tree.description;

import com.agileapes.motorex.string.exception.MissingExpectedTokenException;
import com.agileapes.motorex.string.scan.DocumentScanner;
import com.agileapes.motorex.string.scan.SnippetParser;
import com.agileapes.motorex.string.scan.impl.EnclosedSnippetParser;
import com.agileapes.motorex.string.scan.impl.IdentifierParser;
import com.agileapes.motorex.string.scan.impl.PatternSnippetParser;
import com.agileapes.motorex.string.scan.impl.PositionAwareDocumentScanner;
import com.agileapes.motorex.string.token.Token;
import com.agileapes.motorex.string.token.impl.SimpleToken;
import com.agileapes.motorex.tree.Node;
import com.agileapes.motorex.tree.evaluator.NodeEvaluatorContext;
import com.agileapes.motorex.tree.evaluator.impl.DefaultNodeEvaluatorContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A node description is the key to identifying and querying nodes within a tree data structure.
 * This class cannot be instantiated manually, and you have to compile a textual description into
 * a meaningful node description using {@link #compile(String)} or
 * {@link #compile(String, com.agileapes.motorex.tree.evaluator.NodeEvaluatorContext)}
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 3:13)
 */
public class NodeDescription {

    public static final String STANDARD_QUOTATION = "'\"`";

    private final List<EvaluationDescription> evaluators;
    private final NodeEvaluatorContext evaluatorContext;

    private NodeDescription() {
        this(null);
    }

    private NodeDescription(NodeEvaluatorContext evaluatorContext) {
        if (evaluatorContext == null) {
            evaluatorContext = new DefaultNodeEvaluatorContext();
        }
        evaluators = new ArrayList<EvaluationDescription>();
        this.evaluatorContext = evaluatorContext;
    }

    List<EvaluationDescription> getEvaluators() {
        return evaluators;
    }

    private void addEvaluator(String name, List<String> parameters) {
        evaluators.add(new EvaluationDescription(evaluatorContext.getEvaluator(name), parameters));
    }

    private void addEvaluator(String name, String... parameters) {
        addEvaluator(name, Arrays.asList(parameters));
    }

    /**
     * This method will return a matcher instance for the given node. The matcher will be able
     * to tell whether this node matches the description provided by this instance or not.
     * @param node    the node which is being tested against the description.
     * @return the matcher for this node
     */
    public NodeMatcher matcher(Node node) {
        return new NodeMatcher(this, node);
    }

    /**
     * This method is a shorthand for calling {@link #matcher(Node)}
     * @param node    the node to be tested
     * @return {@code true} if the specified node can be identified using the given description
     */
    public boolean test(Node node) {
        return matcher(node).matches();
    }

    /**
     * This compilation routine will create a valid NodeDescription. However, using this overloaded
     * method, you can only use the default and built-in {@link com.agileapes.motorex.tree.evaluator.NodeEvaluator}s.
     * @param description    the textual description for the node
     * @return an object containing the data model that is equivalent to the provided description.
     * @see #compile(String, com.agileapes.motorex.tree.evaluator.NodeEvaluatorContext)
     */
    public static NodeDescription compile(String description) {
        return compile(description, null);
    }

    /**
     * This method will compile the given textual description into a data model representing that
     * same description. This model can further be used to get a {@link NodeMatcher}, which will
     * help you determine whether a given node matches the compiled description or not.
     *
     * The textual description must match the following syntax:
     *
     * <pre>name index attributes evaluators</pre>
     *
     * All of the four selectors specified above are optional, however, note that an empty
     * descriptor will result in an empty set of evaluators, and thus, since there are no
     * tests to exclude any nodes, all nodes will match that description. Therefore, it is
     * safe to consider the empty description as a sort of <em>catch-all</em> description
     * that will match <em>all</em> nodes.
     *
     * <p><strong>The Syntax</strong></p>
     *
     * <li><strong>name</strong>; the name is a regular expression that will be used to match
     * the name of the node. This means that the query will be run against {@link Node#getName()}.
     * Note that if the regular expression for the name contains {@code #}, {@code [}, or <code>{</code>,
     * all of which are identifier tokens in the query syntax, you <strong>must</strong> enclose
     * the description using one of the three standard quotation designators: {@code '}, {@code "},
     * and {@code `}, otherwise, using the quotation to separate the pattern from the rest of your
     * description is optional. The quotations support escaping, meaning that you can use the
     * escaped form of a given quotation character inside a text enclosed by it.</li>
     *
     * <li><strong>index</strong>; the index refers to the numerical index of any given node
     * in regards to its siblings. For the root node, which by definition has no siblings this
     * number will always be {@code 0}. Note that indexing starts from {@code 0} and goes up
     * sequentially, and is updated automatically to reflect the current status of the nodes
     * and the tree as a whole. To query the index of any given node, you will have to start
     * with the character {@code #}, followed immediately by a valid, positive integer.
     * This query will use the result of {@link Node#getIndex()} to determine the results.</li>
     *
     * <li><strong>attributes</strong>; attributes selectors must be enclosed within a pair
     * of matching square brackets ({@code []}). Inside these, comes a list of attribute
     * criteria, which are of the form {@code name[=value]} separated by commas ({@code ,}) internally.
     * As you can see, the value is optional. If no value is specified, the simple existence of
     * that attribute will suffice for the node to match the given description. The attribute
     * name is a regular expression, as is the attribute value. As is the case with the node name,
     * you are expected to compensate the irregularities in your query by enclosing the text
     * with one of the provided standard quotation pairs.</li>
     *
     * <li><strong>evaluators</strong>; evaluators are essentially test functions that can
     * determine whether any given node is a match for the description or not. These are all
     * implementations of {@link com.agileapes.motorex.tree.evaluator.NodeEvaluator} and must
     * be mentioned using the name by which they are known to the used {@link NodeEvaluatorContext}.
     * The list of evaluators comes enclosed in a pair of curly brackets (<code>{}</code>). Each evaluator
     * is of the form {@code name(param1, param2, ...);}. The semi-colon is optional for the last evaluator
     * to occur. The order in which evaluators are executed is preserved by the compiler and they
     * occur as they have been mentioned in the description, though this should not affect the outcome
     * of the evaluation, but only the performance and speed by which it is done. The parameters must be
     * enclosed with the quotation characters and can contain escaped text, unless they are guaranteed to
     * not contain any of these characters: <code>,</code>, <code>(</code>, <code>)</code>, <code>{</code>,
     * or <code>}</code></li>
     *
     * <p><strong>Usage</strong></p>
     *
     * The usage of the node description engine is quite simple. It generally takes one of the following
     * two forms:
     *
     * <pre>
     *     if (NodeDescription.compile("description").matcher(node).matches()) {
     *         //do something
     *     }</pre>
     *
     * or the shorter form:
     *
     * <pre>
     *     if (NodeDescription.compile("description").test(node)) {
     *         //do something
     *     }</pre>
     *
     * @param description         the textual description for the node
     * @param evaluatorContext    the context object containing named evaluators for the description.
     *                            The context is expected to hold at least the three built-in evaluators
     *                            against which the most basic tests can run.
     * @return an object containing the data model that is equivalent to the provided description.
     * @see #compile(String)
     */
    public static NodeDescription compile(final String description, final NodeEvaluatorContext evaluatorContext) {
        if (description == null) {
            throw new NullPointerException("Description cannot be null");
        }
        final NodeDescription nodeDescription = new NodeDescription(evaluatorContext);
        final DocumentScanner scanner = new PositionAwareDocumentScanner(description, new PatternSnippetParser("\\S+"));
        scanner.read("\\s+");
        if (scanner.remaining() > 0) {
            //if the description was not empty we will proceed to scan the four sections
            parseNodeName(nodeDescription, scanner);
            parseNodeIndex(nodeDescription, scanner);
            parseNodeAttributes(description, nodeDescription, scanner);
            parseNodeEvaluators(nodeDescription, scanner);
            scanner.read("\\s*");
            if (scanner.remaining() > 0) {
                throw new IllegalArgumentException("Unexpected input: " + scanner.getRemainder());
            }
        }
        return nodeDescription;
    }

    private static void parseNodeEvaluators(final NodeDescription nodeDescription, DocumentScanner scanner) {
        scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                final int offset = scanner.getOffset();
                //first we skip all whitespaces
                scanner.read("\\s*");
                if (!scanner.has("{")) {
                    //maybe no evaluators have been specified
                    return null;
                }
                //then we take off the opening '{'
                scanner.expect("{");
                scanner.read("\\s*");
                while (scanner.remaining() > 0 && !scanner.has("}")) {
                    //we read an identifier, and expect it to be the name of an evaluator
                    final String evaluator = scanner.parse(new IdentifierParser());
                    final List<String> parameters = new ArrayList<String>();
                    //then we go for the `(param1, param2, ...)` syntax
                    scanner.read("\\s*");
                    scanner.expect("(");
                    scanner.read("\\s*");
                    while (!scanner.has(")")) {
                        //the fallback method for reading an unenclosed parameter is one that matches the given pattern
                        final String parameter = scanner.parse(new EnclosedSnippetParser(STANDARD_QUOTATION, STANDARD_QUOTATION, new PatternSnippetParser("[^,\\(\\)\\{\\}]+"), true, true, '\\'));
                        if (scanner.has("'", "\"", "`")) {
                            scanner.read();
                        }
                        //the discovered parameter will be added to the list of the evaluators parameters
                        parameters.add(parameter);
                        scanner.read("\\s*");
                        if (scanner.has(",")) {
                            scanner.read();
                            scanner.read("\\s*");
                        } else {
                            if (!scanner.has(")")) {
                                throw new MissingExpectedTokenException();
                            }
                        }
                    }
                    //and now we close the list of parameters
                    scanner.expect(")");
                    //if the ';' has been omitted, we MUST have reached the closing '}'
                    nodeDescription.addEvaluator(evaluator, parameters);
                    if (scanner.has(";")) {
                        scanner.read();
                        scanner.read("\\s*");
                    } else if (!scanner.has("}")) {
                        throw new MissingExpectedTokenException();
                    }
                }
                //closing the list of evaluators
                scanner.expect("}");
                return new SimpleToken(scanner.getOffset() - offset);
             }
        });
    }

    private static void parseNodeAttributes(final String description, final NodeDescription nodeDescription, DocumentScanner scanner) {
        scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                final int offset = scanner.getOffset();
                scanner.read("\\s*");
                if (!scanner.has("[")) {
                    //maybe no attribute selectors have been defined
                    return null;
                }
                scanner.expect('[');
                while (scanner.remaining() > 0 && !scanner.has("]")) {
                    //reading until we reach either ',', '=', or ']'
                    final String attribute = scanner.parse(new EnclosedSnippetParser(STANDARD_QUOTATION, STANDARD_QUOTATION, new PatternSnippetParser("[^,\\]=]+"), true, true, '\\'));
                    if (scanner.has("'", "\"", "`")) {
                        scanner.read();
                    }
                    final String value;
                    //now we will see if a value has been provided
                    if (scanner.has("=")) {
                        scanner.read();
                        value = scanner.parse(new EnclosedSnippetParser(STANDARD_QUOTATION, STANDARD_QUOTATION, new PatternSnippetParser("\\w+"), true, true, '\\'));
                        if (scanner.has("'", "\"", "`")) {
                            scanner.read();
                        }
                    } else {
                        value = "";
                    }
                    if (attribute.isEmpty() && value.isEmpty()) {
                        throw new IllegalArgumentException("You have to at least specify an attribute and value in " + description);
                    }
                    if (value.isEmpty()) {
                        nodeDescription.addEvaluator("$attribute", attribute);
                    } else {
                        nodeDescription.addEvaluator("$attribute", attribute, value);
                    }
                    if (!scanner.has("]")) {
                        scanner.expect(",");
                    }
                }
                scanner.expect("]");
                return new SimpleToken(scanner.getOffset() - offset);
            }
        });
    }

    private static void parseNodeIndex(final NodeDescription nodeDescription, DocumentScanner scanner) {
        scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                final int offset = scanner.getOffset();
                scanner.read("\\s*");
                if (!scanner.has("#")) {
                    return null;
                }
                //reading the index opener ...
                scanner.expect('#');
                //and then the integer value itself
                final String indexStr = scanner.expect(Pattern.compile("\\d+"));
                final int index;
                try {
                    index = Integer.parseInt(indexStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Not a valid integer: " + indexStr);
                }
                nodeDescription.addEvaluator("$index", String.valueOf(index));
                return new SimpleToken(scanner.getOffset() - offset);
            }
        });
    }

    private static void parseNodeName(final NodeDescription nodeDescription, DocumentScanner scanner) {
        scanner.parse(new SnippetParser() {
            @Override
            public Token parse(DocumentScanner scanner) {
                //if the description starts with something other than a name pattern
                final int start = scanner.getOffset();
                if (scanner.has("#") || scanner.has("[") || scanner.has("{")) {
                    return null;
                }
                String name = "";
                while (scanner.remaining() > 0) {
                    final String next = scanner.peek(1);
                    if (STANDARD_QUOTATION.contains(next)) {
                        name += next;
                        name += scanner.parse(new EnclosedSnippetParser(next, next, null, false, true, '\\'));
                        name += scanner.expect(next.charAt(0));
                        continue;
                    } else if (scanner.has("#", "[", "{")) {
                        break;
                    }
                    name += scanner.read();
                }
                nodeDescription.addEvaluator("$name", name.trim());
                return new SimpleToken(scanner.getOffset() - start);
            }
        });
    }

}
