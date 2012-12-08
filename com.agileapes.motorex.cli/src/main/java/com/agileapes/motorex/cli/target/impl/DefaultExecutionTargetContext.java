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

import com.agileapes.motorex.cli.exception.DuplicateExecutionTargetException;
import com.agileapes.motorex.cli.exception.NoSuchExecutionTargetException;
import com.agileapes.motorex.cli.target.DefaultExecutionTarget;
import com.agileapes.motorex.cli.target.ExecutionTarget;
import com.agileapes.motorex.cli.target.ExecutionTargetContext;
import com.agileapes.motorex.cli.target.ExecutionTargetRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:36)
 */
public class DefaultExecutionTargetContext implements ExecutionTargetContext, ExecutionTargetRegistry {

    private Map<String, ExecutionTarget> targets = new HashMap<String, ExecutionTarget>();
    private ExecutionTarget defaultTarget = null;

    @Override
    public ExecutionTarget getDefaultTarget() throws NoSuchExecutionTargetException {
        if (defaultTarget == null) {
            throw new NoSuchExecutionTargetException("(default)");
        }
        return defaultTarget;
    }

    @Override
    public ExecutionTarget getExecutionTarget(String identifier) throws NoSuchExecutionTargetException {
        if (!targets.containsKey(identifier)) {
            throw new NoSuchExecutionTargetException(identifier);
        }
        return targets.get(identifier);
    }

    @Override
    public Set<String> getTargets() {
        return targets.keySet();
    }

    @Override
    public void register(ExecutionTarget executionTarget) throws DuplicateExecutionTargetException {
        if (targets.containsKey(executionTarget.getIdentifier())) {
            throw new DuplicateExecutionTargetException(executionTarget.getIdentifier());
        }
        if (executionTarget instanceof DefaultExecutionTarget) {
            defaultTarget = executionTarget;
        }
        targets.put(executionTarget.getIdentifier(), executionTarget);
    }

}
