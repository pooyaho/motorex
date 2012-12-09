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

import com.agileapes.motorex.cli.config.ConfigurationBuilder;
import com.agileapes.motorex.cli.config.impl.ArrayConfigurationBuilder;
import com.agileapes.motorex.cli.config.impl.StandardSpecificationBuilder;
import com.agileapes.motorex.cli.target.ExecutionTarget;
import com.agileapes.motorex.cli.target.ExecutionTargetContext;
import com.agileapes.motorex.cli.target.ExecutionTargetContextAware;
import com.agileapes.motorex.cli.value.ValueReaderContext;
import com.agileapes.motorex.cli.value.ValueReaderContextAware;

/**
 * This class will put together the magic provided by value readers and execution targets
 * and through the given input array, will execute the correct target using the proper
 * variable arguments.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:32)
 */
public class ExecutionConfigurator implements ExecutionTargetContextAware, ValueReaderContextAware {

    private ExecutionTargetContext context;
    private ValueReaderContext valueReaderContext;

    @Override
    public void setExecutionTargetContext(ExecutionTargetContext context) {
        this.context = context;
    }

    @Override
    public void setValueReaderContext(ValueReaderContext valueReaderContext) {
        this.valueReaderContext = valueReaderContext;
    }

    public void execute(String... arguments) {
        try {
            final ExecutionTarget target;
            final ConfigurationBuilder configurationBuilder;
            if (arguments.length == 0 || arguments[0].startsWith("-")) {
                target = context.getDefaultTarget();
                configurationBuilder = new ArrayConfigurationBuilder(new StandardSpecificationBuilder(target).build(), 0, arguments);
            } else {
                target = context.getExecutionTarget(arguments[0]);
                configurationBuilder = new ArrayConfigurationBuilder(new StandardSpecificationBuilder(target).build(), 1, arguments);
            }
            ((ValueReaderContextAware) configurationBuilder).setValueReaderContext(valueReaderContext);
            target.execute(configurationBuilder.build());
        } catch (Error e) {
            System.err.println(e.getMessage());
        }
    }

}
