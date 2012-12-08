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

import com.agileapes.motorex.tree.exception.NoSuchEvaluatorException;

/**
 * This interface will enable the user to find evaluators by their aliases, and then use them
 * in their queries
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 3:33)
 */
public interface NodeEvaluatorContext {

    /**
     * Will look for an evaluator with the given alias
     * @param name    evaluator name
     * @return the evaluator
     * @throws NoSuchEvaluatorException if the name has no corresponding evaluator
     */
    NodeEvaluator getEvaluator(String name) throws NoSuchEvaluatorException;

}
