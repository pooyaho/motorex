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

import com.agileapes.motorex.cli.config.Option;
import com.agileapes.motorex.cli.target.ExecutionTarget;

import java.util.Arrays;
import java.util.List;

/**
 * This abstract class is provided to be used as a support by execution targets,
 * so that they would not need to implement the more redundant methods. Also, this
 * class is used by the framework to replace Spring been names with execution
 * target identifiers.
 *
 * So, if your execution target does not extend this class, you will have to hard
 * code the identifier inside the code, or devise another method.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:28)
 */
public abstract class AbstractIdentifiedExecutionTarget implements ExecutionTarget {

    private String identifier;
    private final List<Option> options;

    protected AbstractIdentifiedExecutionTarget(List<Option> options) {
        this.options = options;
    }

    protected AbstractIdentifiedExecutionTarget(Option... options) {
        this(Arrays.asList(options));
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public List<Option> getOptions() {
        return options;
    }

}
