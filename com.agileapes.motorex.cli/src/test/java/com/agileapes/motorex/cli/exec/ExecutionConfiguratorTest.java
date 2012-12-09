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

package com.agileapes.motorex.cli.exec;

import com.agileapes.motorex.cli.config.Configuration;
import com.agileapes.motorex.cli.target.DefaultExecutionTarget;
import com.agileapes.motorex.cli.target.impl.AbstractIdentifiedExecutionTarget;
import com.agileapes.motorex.cli.target.impl.DefaultExecutionTargetContext;
import com.agileapes.motorex.cli.value.impl.DefaultValueReaderContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/9, 3:22)
 */
public class ExecutionConfiguratorTest {

    private static final class NormalTarget extends AbstractIdentifiedExecutionTarget implements DefaultExecutionTarget {

        private final Writer writer;

        private NormalTarget(Writer writer) {
            this.writer = writer;
            setIdentifier("normal");
        }

        @Override
        public void execute(Configuration configuration) {
            try {
                writer.append("this is a test ");
            } catch (IOException ignored) {
            }
        }

        public Writer getWriter() {
            return writer;
        }

    }

    private static final class ErrorProneTarget extends AbstractIdentifiedExecutionTarget {

        private ErrorProneTarget() {
            setIdentifier("error-prone");
        }

        @Override
        public void execute(Configuration configuration) {
            throw new Error("this is the message");
        }

    }

    private DefaultExecutionTargetContext getExecutionTargetContext(StringWriter writer) {
        final DefaultExecutionTargetContext context = new DefaultExecutionTargetContext();
        context.register(new NormalTarget(writer));
        context.register(new ErrorProneTarget());
        return context;
    }

    private DefaultValueReaderContext getValueReaderContext() {
        return new DefaultValueReaderContext();
    }

    @Test
    public void testConfigurationOfNormalTarget() throws Exception {
        final ExecutionConfigurator configurator = new ExecutionConfigurator();
        final StringWriter writer = new StringWriter();
        configurator.setExecutionTargetContext(getExecutionTargetContext(writer));
        configurator.setValueReaderContext(getValueReaderContext());
        configurator.execute("normal");
        Assert.assertEquals(writer.toString(), "this is a test ");
        configurator.execute();
        Assert.assertEquals(writer.toString(), "this is a test this is a test ");
    }

    @Test
    public void testErroneousExecution() throws Exception {
        final ExecutionConfigurator configurator = new ExecutionConfigurator();
        configurator.setValueReaderContext(getValueReaderContext());
        configurator.setExecutionTargetContext(getExecutionTargetContext(null));
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream err = System.err;
        System.setErr(new PrintStream(out));
        configurator.execute("error-prone");
        System.setErr(err);
        final String string = out.toString();
        Assert.assertEquals(string, "this is the message\n");
    }

}
