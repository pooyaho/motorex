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

import com.agileapes.motorex.cli.exception.DuplicateExecutionTargetException;

/**
 * This interface exposes registry functionality with the framework, so that
 * execution targets can be registered and held within a single point.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:36)
 */
public interface ExecutionTargetRegistry {

    /**
     * Will attempt to register the given target with the application
     * @param executionTarget    the execution target instance
     * @throws DuplicateExecutionTargetException
     */
    void register(ExecutionTarget executionTarget) throws DuplicateExecutionTargetException;

}
