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

package com.agileapes.motorex.cli.target.impl;

import com.agileapes.motorex.cli.config.Configuration;
import com.agileapes.motorex.cli.config.Option;
import com.agileapes.motorex.cli.config.impl.NormalFlag;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/9, 3:18)
 */
public class AbstractIdentifiedExecutionTargetTest {

    private static final class TestTarget extends AbstractIdentifiedExecutionTarget {

        private TestTarget(Option... options) {
            super(options);
        }

        @Override
        public void execute(Configuration configuration) {
        }

    }

    @Test
    public void testFunctionality() throws Exception {
        final TestTarget target = new TestTarget(new NormalFlag("1"), new NormalFlag("2"));
        Assert.assertNull(target.getIdentifier());
        target.setIdentifier("identifier");
        Assert.assertEquals(target.getIdentifier(), "identifier");
        Assert.assertNotNull(target.getOptions());
        Assert.assertFalse(target.getOptions().isEmpty());
        Assert.assertEquals(target.getOptions().size(), 2);
    }

}
