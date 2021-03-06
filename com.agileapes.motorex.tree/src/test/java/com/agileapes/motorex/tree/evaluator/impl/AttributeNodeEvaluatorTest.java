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

import com.agileapes.motorex.tree.evaluator.NodeEvaluatorTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 2:49)
 */
public class AttributeNodeEvaluatorTest extends NodeEvaluatorTest {

    private boolean test(String... parameters) {
        return new AttributeNodeEvaluator().test(getNode(), Arrays.asList(parameters));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooFewArguments() throws Exception {
        test();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooManyArguments() throws Exception {
        test("1", "2", "3");
    }

    @Test
    public void testAttributeExistence() throws Exception {
        Assert.assertFalse(test("mno"));
        Assert.assertFalse(test("\\d+"));
        Assert.assertTrue(test("[a-z]+"));
        Assert.assertTrue(test("age"));
        Assert.assertTrue(test("n.*"));
        Assert.assertTrue(test(".*x.*"));
    }

    @Test
    public void testAttributeValue() throws Exception {
        Assert.assertFalse(test(".*", "testing"));
        Assert.assertFalse(test(".*", "\\d+\\.\\d+"));
        Assert.assertTrue(test(".*", "\\d+"));
        Assert.assertTrue(test(".*", ""));
        Assert.assertTrue(test(".*", "[a-z]+"));
    }

    @Test
    public void testAttributeAndValue() throws Exception {
        Assert.assertFalse(test("name", "\\d+"));
        Assert.assertTrue(test("a.*", "\\d+"));
    }
}
