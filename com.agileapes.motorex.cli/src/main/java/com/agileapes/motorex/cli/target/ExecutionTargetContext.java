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

package com.agileapes.motorex.cli.target;

import com.agileapes.motorex.cli.exception.NoSuchExecutionTargetException;

import java.util.Set;

/**
 * The execution target context will hold information regarding the execution
 * targets defined within the application, and make them accessible through their
 * unique identifiers ({@link ExecutionTarget#getIdentifier()})
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:32)
 */
public interface ExecutionTargetContext {

    /**
     * @return the default execution target if one is defined
     * @throws NoSuchExecutionTargetException
     */
    ExecutionTarget getDefaultTarget() throws NoSuchExecutionTargetException;

    /**
     * @param identifier    the name of the desired target
     * @return the execution target, if it exists
     * @throws NoSuchExecutionTargetException
     */
    ExecutionTarget getExecutionTarget(String identifier) throws NoSuchExecutionTargetException;

    /**
     * @return the names of all of the available execution targets
     */
    Set<String> getTargets();

}
