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
import com.agileapes.motorex.tree.Node;
import com.agileapes.motorex.tree.evaluator.NodeEvaluator;
import com.agileapes.motorex.tree.evaluator.NodeEvaluatorContext;
import com.agileapes.motorex.tree.evaluator.impl.AttributeNodeEvaluator;
import com.agileapes.motorex.tree.evaluator.impl.DefaultNodeEvaluatorContext;
import com.agileapes.motorex.tree.evaluator.impl.IndexNodeEvaluator;
import com.agileapes.motorex.tree.evaluator.impl.NameNodeEvaluator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 5:49)
 */
public class NodeDescriptionTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testCompileNull() throws Exception {
        NodeDescription.compile(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIrregularSyntaxWithTrailingTokens() throws Exception {
        NodeDescription.compile("[] test");
    }

    @Test
    public void testCompileNodeNameOnly() throws Exception {
        final NodeDescription description = NodeDescription.compile("node name");
        final List<EvaluationDescription> evaluators = description.getEvaluators();
        Assert.assertFalse(evaluators.isEmpty());
        Assert.assertEquals(evaluators.size(), 1);
        final EvaluationDescription evaluationDescription = evaluators.get(0);
        Assert.assertTrue(evaluationDescription.getEvaluator() instanceof NameNodeEvaluator);
        Assert.assertEquals(evaluationDescription.getParameters().size(), 1);
        Assert.assertEquals(evaluationDescription.getParameters().get(0), "node name");
    }

    @Test
    public void testCompileNodeNameWithQuotationOnly() throws Exception {
        final NodeDescription description = NodeDescription.compile("'node name'");
        final List<EvaluationDescription> evaluators = description.getEvaluators();
        Assert.assertFalse(evaluators.isEmpty());
        Assert.assertEquals(evaluators.size(), 1);
        final EvaluationDescription evaluationDescription = evaluators.get(0);
        Assert.assertTrue(evaluationDescription.getEvaluator() instanceof NameNodeEvaluator);
        Assert.assertEquals(evaluationDescription.getParameters().size(), 1);
        Assert.assertEquals(evaluationDescription.getParameters().get(0), "node name");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*integer.*")
    public void testCompileInvalidIndex() throws Exception {
        NodeDescription.compile("#hello");
    }

    @Test
    public void testCompileIndexOnly() throws Exception {
        final NodeDescription description = NodeDescription.compile("#345");
        final List<EvaluationDescription> evaluators = description.getEvaluators();
        Assert.assertFalse(evaluators.isEmpty());
        Assert.assertEquals(evaluators.size(), 1);
        final EvaluationDescription evaluationDescription = evaluators.get(0);
        Assert.assertTrue(evaluationDescription.getEvaluator() instanceof IndexNodeEvaluator);
        Assert.assertEquals(evaluationDescription.getParameters().size(), 1);
        Assert.assertEquals(evaluationDescription.getParameters().get(0), "345");
    }

    @Test
    public void testCompileAttributesOnly() throws Exception {
        final NodeDescription description = NodeDescription.compile("['a',b=xyz,c.*='\\d+']");
        final List<EvaluationDescription> evaluators = description.getEvaluators();
        Assert.assertEquals(evaluators.size(), 3);
        //#1
        final EvaluationDescription first = evaluators.get(0);
        Assert.assertTrue(first.getEvaluator() instanceof AttributeNodeEvaluator);
        Assert.assertEquals(first.getParameters().size(), 1);
        Assert.assertEquals(first.getParameters().get(0), "a");
        //#2
        final EvaluationDescription second = evaluators.get(1);
        Assert.assertTrue(second.getEvaluator() instanceof AttributeNodeEvaluator);
        Assert.assertEquals(second.getParameters().size(), 2);
        Assert.assertEquals(second.getParameters().get(0), "b");
        Assert.assertEquals(second.getParameters().get(1), "xyz");
        //#3
        final EvaluationDescription third = evaluators.get(2);
        Assert.assertTrue(third.getEvaluator() instanceof AttributeNodeEvaluator);
        Assert.assertEquals(third.getParameters().size(), 2);
        Assert.assertEquals(third.getParameters().get(0), "c.*");
        Assert.assertEquals(third.getParameters().get(1), "\\d+");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*attribute.*")
    public void testIrregularAttributeListing() throws Exception {
        NodeDescription.compile("[,]");
    }

    @Test
    public void testNameWithEmptyAttributes() throws Exception {
        final NodeDescription description = NodeDescription.compile("node name[]");
        final List<EvaluationDescription> evaluators = description.getEvaluators();
        Assert.assertFalse(evaluators.isEmpty());
        Assert.assertEquals(evaluators.size(), 1);
        final EvaluationDescription evaluationDescription = evaluators.get(0);
        Assert.assertTrue(evaluationDescription.getEvaluator() instanceof NameNodeEvaluator);
        Assert.assertEquals(evaluationDescription.getParameters().size(), 1);
        Assert.assertEquals(evaluationDescription.getParameters().get(0), "node name");
    }

    @Test
    public void testEvaluatorsListOnly() throws Exception {
        final NodeDescription description = NodeDescription.compile("{e1(); e2(1,2,3); e3('()','}');}", getContext("e1", "e2", "e3"));
        final List<EvaluationDescription> evaluators = description.getEvaluators();
        Assert.assertEquals(evaluators.size(), 3);
        final EvaluationDescription first = evaluators.get(0);
        final EvaluationDescription second = evaluators.get(1);
        final EvaluationDescription third = evaluators.get(2);
        Assert.assertTrue(first.getParameters().isEmpty());
        Assert.assertFalse(second.getParameters().isEmpty());
        Assert.assertFalse(third.getParameters().isEmpty());
        Assert.assertEquals(second.getParameters().size(), 3);
        Assert.assertEquals(second.getParameters().get(0), "1");
        Assert.assertEquals(second.getParameters().get(1), "2");
        Assert.assertEquals(second.getParameters().get(2), "3");
        Assert.assertEquals(third.getParameters().size(), 2);
        Assert.assertEquals(third.getParameters().get(0), "()");
        Assert.assertEquals(third.getParameters().get(1), "}");
    }

    @Test(expectedExceptions = MissingExpectedTokenException.class)
    public void testEvaluatorListingWithMissingCloser() throws Exception {
        NodeDescription.compile("{hello()", getContext("hello"));
    }

    @Test(expectedExceptions = MissingExpectedTokenException.class)
    public void testEvaluatorListingWithMismatchedParenthesis() throws Exception {
        NodeDescription.compile("{hello(); there(}", getContext("hello", "there"));
    }

    private HashMap<String, NodeEvaluator> getEvaluators(String... names) {
        final HashMap<String, NodeEvaluator> map = new HashMap<String, NodeEvaluator>();
        for (String name : names) {
            map.put(name, new NodeEvaluator() {
                @Override
                public boolean test(Node node, List<String> parameters) {
                    return false;
                }
            });
        }
        return map;
    }

    private NodeEvaluatorContext getContext(String... evaluators) {
        return new DefaultNodeEvaluatorContext(getEvaluators(evaluators));
    }

}
