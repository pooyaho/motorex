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

package com.agileapes.motorex.cli.config.impl;

import com.agileapes.motorex.cli.config.Configuration;
import com.agileapes.motorex.cli.config.Option;
import com.agileapes.motorex.cli.config.Specification;
import com.agileapes.motorex.cli.target.impl.AbstractIdentifiedExecutionTarget;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/9, 2:47)
 */
public class StandardSpecificationBuilderTest {

    private static final class TestTarget extends AbstractIdentifiedExecutionTarget {

        private TestTarget() {
            super(new NormalFlag("a", '1'), new NormalFlag("b", '2'));
        }

        @Override
        public void execute(Configuration configuration) {
        }

    }

    @Test
    public void testBuild() throws Exception {
        final Specification specification = new StandardSpecificationBuilder(new TestTarget()).build();
        Assert.assertFalse(specification.getOptions().isEmpty());
        Assert.assertEquals(specification.getOptions().size(), 2);
        for (Option option : specification.getOptions()) {
            Assert.assertTrue(option instanceof NormalFlag);
            if (option.name().equals("a")) {
                Assert.assertTrue(option.isFlag());
                Assert.assertEquals(option.shorthand(), (Character) '1');
            } else if (option.name().equals("b")) {
                Assert.assertTrue(option.isFlag());
                Assert.assertEquals(option.shorthand(), (Character) '2');
            } else {
                Assert.fail();
            }
        }
    }
}
