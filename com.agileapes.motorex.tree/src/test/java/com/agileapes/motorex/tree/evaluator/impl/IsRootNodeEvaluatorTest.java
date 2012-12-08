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
import com.agileapes.motorex.tree.evaluator.NodeEvaluatorTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 6:27)
 */
public class IsRootNodeEvaluatorTest extends NodeEvaluatorTest {

    private boolean test(Node node, String... parameters) {
        return new IsRootNodeEvaluator().test(node, Arrays.asList(parameters));
    }

    private boolean test(String... parameters) {
        return test(getNode(), parameters);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooManyArguments() throws Exception {
        test("1");
    }

    @Test
    public void testNotMatch() throws Exception {
        Assert.assertFalse(test(getNode().getFirstChild()));
    }

    @Test
    public void testMatch() throws Exception {
        Assert.assertTrue(test());
    }
}
