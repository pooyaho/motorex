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

package com.agileapes.motorex.tree.evaluator;

import com.agileapes.motorex.tree.exception.DuplicateEvaluatorException;

/**
 * Will enable users to develop registries of NodeEvaluators and register the
 * evaluators in a standard fashion
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 3:34)
 */
public interface NodeEvaluatorRegistry {

    /**
     * Will attempt to register the given evaluator with the provided alias
     * @param name         the name of the evaluator
     * @param evaluator    the node evaluator instance
     * @throws DuplicateEvaluatorException if another evaluator with the specified
     * name has been declared.
     */
    void register(String name, NodeEvaluator evaluator) throws DuplicateEvaluatorException;

}
