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

package com.agileapes.motorex.tree.evaluator.impl;

import com.agileapes.motorex.tree.Node;
import com.agileapes.motorex.tree.evaluator.NodeEvaluator;
import com.agileapes.motorex.tree.exception.DuplicateEvaluatorException;
import com.agileapes.motorex.tree.exception.NoSuchEvaluatorException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 6:42)
 */
public class DefaultNodeEvaluatorContextTest {

    @Test
    public void testGettingExistingEvaluator() throws Exception {
        final NodeEvaluator evaluator = new DefaultNodeEvaluatorContext().getEvaluator("$name");
        Assert.assertNotNull(evaluator);
        Assert.assertTrue(evaluator instanceof NameNodeEvaluator);
    }

    @Test(expectedExceptions = NoSuchEvaluatorException.class)
    public void testGettingNonExistentEvaluator() throws Exception {
        new DefaultNodeEvaluatorContext().getEvaluator("myEvaluator");
    }

    @Test
    public void testRegisteringNewEvaluator() throws Exception {
        final String test = "this is a test";
        final DefaultNodeEvaluatorContext context = new DefaultNodeEvaluatorContext();
        context.register("myEvaluator", new NodeEvaluator() {
            @Override
            public boolean test(Node node, List<String> parameters) {
                return parameters.get(0).equals(test);
            }
        });
        final NodeEvaluator evaluator = context.getEvaluator("myEvaluator");
        Assert.assertNotNull(evaluator);
        Assert.assertFalse(evaluator.test(null, Arrays.asList("")));
        Assert.assertTrue(evaluator.test(null, Arrays.asList(test)));
    }

    @Test(expectedExceptions = DuplicateEvaluatorException.class)
    public void testRegisterDuplicateName() throws Exception {
        new DefaultNodeEvaluatorContext().register("$name", null);
    }

    @Test
    public void testBatchRegistration() throws Exception {
        final HashMap<String, NodeEvaluator> map = new HashMap<String, NodeEvaluator>();
        final NodeEvaluator evaluator = new NodeEvaluator() {
            @Override
            public boolean test(Node node, List<String> parameters) {
                return false;
            }
        };
        map.put("e1", evaluator);
        map.put("e2", evaluator);
        map.put("e3", evaluator);
        final DefaultNodeEvaluatorContext context = new DefaultNodeEvaluatorContext(map);
        Assert.assertNotNull(context.getEvaluator("e1"));
        Assert.assertNotNull(context.getEvaluator("e2"));
        Assert.assertNotNull(context.getEvaluator("e3"));
    }

}
