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
import com.agileapes.motorex.cli.exception.DuplicateExecutionTargetException;
import com.agileapes.motorex.cli.exception.MultipleDefaultExecutionTargetsException;
import com.agileapes.motorex.cli.exception.NoSuchExecutionTargetException;
import com.agileapes.motorex.cli.target.DefaultExecutionTarget;
import com.agileapes.motorex.cli.target.ExecutionTarget;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/9, 3:03)
 */
public class DefaultExecutionTargetContextTest {

    public abstract static class Target extends AbstractIdentifiedExecutionTarget {

        private final Writer writer;

        public Target(Writer writer) {
            super();
            this.writer = writer;
        }

        @Override
        public void execute(Configuration configuration) {
            try {
                writer.append(getWord());
            } catch (IOException ignored) {
            }
        }

        protected abstract String getWord();

        @Override
        public String getIdentifier() {
            return getWord();
        }

        public Writer getWriter() {
            return writer;
        }

    }

    private static final class First extends Target {

        public First(Writer writer) {
            super(writer);
        }

        @Override
        protected String getWord() {
            return "first";
        }

    }

    private static final class Second extends Target implements DefaultExecutionTarget {

        public Second(Writer writer) {
            super(writer);
        }

        @Override
        protected String getWord() {
            return "second";
        }

    }

    private static final class SecondFirst /*get that?!*/ extends Target implements DefaultExecutionTarget {

        public SecondFirst(Writer writer) {
            super(writer);
        }

        @Override
        protected String getWord() {
            return "first";
        }
    }

    @Test(expectedExceptions = NoSuchExecutionTargetException.class)
    public void testGetDefaultWithNoDefaultSpecified() throws Exception {
        new DefaultExecutionTargetContext().getDefaultTarget();
    }

    @Test(expectedExceptions = NoSuchExecutionTargetException.class)
    public void testRegistration() throws Exception {
        final DefaultExecutionTargetContext context = new DefaultExecutionTargetContext();
        final StringWriter writer = new StringWriter();
        context.register(new First(writer));
        final ExecutionTarget first = context.getExecutionTarget("first");
        Assert.assertNotNull(first);
        Assert.assertEquals(first.getIdentifier(), "first");
        first.execute(null);
        Assert.assertEquals(writer.toString(), "first");
        context.register(new Second(writer));
        Assert.assertEquals(context.getTargets().size(), 2);
        Assert.assertTrue(context.getTargets().contains("first"));
        Assert.assertTrue(context.getTargets().contains("second"));
        context.getExecutionTarget("third");
    }

    @Test
    public void testRegistrationOfDefaultTarget() throws Exception {
        final DefaultExecutionTargetContext context = new DefaultExecutionTargetContext();
        final StringWriter writer = new StringWriter();
        context.register(new Second(writer));
        final ExecutionTarget defaultTarget = context.getDefaultTarget();
        Assert.assertNotNull(defaultTarget);
        Assert.assertEquals(defaultTarget.getIdentifier(), "second");
        defaultTarget.execute(null);
        Assert.assertEquals(writer.toString(), "second");
    }

    @Test(expectedExceptions = DuplicateExecutionTargetException.class)
    public void testRegistrationOfDuplicateTargets() throws Exception {
        final DefaultExecutionTargetContext context = new DefaultExecutionTargetContext();
        context.register(new First(null));
        context.register(new SecondFirst(null));
    }

    @Test(expectedExceptions = MultipleDefaultExecutionTargetsException.class)
    public void testRegistrationOfMultipleDefaultTargets() throws Exception {
        final DefaultExecutionTargetContext context = new DefaultExecutionTargetContext();
        context.register(new Second(null));
        context.register(new SecondFirst(null));
    }

}
