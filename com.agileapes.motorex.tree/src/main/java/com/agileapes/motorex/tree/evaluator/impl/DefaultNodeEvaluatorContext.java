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

package com.agileapes.motorex.tree.evaluator.impl;

import com.agileapes.motorex.tree.evaluator.NodeEvaluator;
import com.agileapes.motorex.tree.evaluator.NodeEvaluatorContext;
import com.agileapes.motorex.tree.evaluator.NodeEvaluatorRegistry;
import com.agileapes.motorex.tree.exception.DuplicateEvaluatorException;
import com.agileapes.motorex.tree.exception.NoSuchEvaluatorException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 3:37)
 */
public class DefaultNodeEvaluatorContext implements NodeEvaluatorContext, NodeEvaluatorRegistry {

    private final Map<String, NodeEvaluator> evaluators;

    public DefaultNodeEvaluatorContext() {
        evaluators = new HashMap<String, NodeEvaluator>();
        evaluators.put("$name", new NameNodeEvaluator());
        evaluators.put("$attribute", new AttributeNodeEvaluator());
        evaluators.put("$index", new IndexNodeEvaluator());
        evaluators.put("isRoot", new IsRootNodeEvaluator());
        evaluators.put("isLeaf", new IsLeafNodeEvaluator());
        evaluators.put("nextTo", new NextToNodeEvaluator());
        evaluators.put("previousTo", new PreviousToNodeEvaluator());
    }

    public DefaultNodeEvaluatorContext(Map<String, NodeEvaluator> evaluators) {
        this();
        this.evaluators.putAll(evaluators);
    }

    @Override
    public NodeEvaluator getEvaluator(String name) throws NoSuchEvaluatorException {
        if (!evaluators.containsKey(name)) {
            throw new NoSuchEvaluatorException(name);
        }
        return evaluators.get(name);
    }

    @Override
    public void register(String name, NodeEvaluator evaluator) throws DuplicateEvaluatorException {
        if (evaluators.containsKey(name)) {
            throw new DuplicateEvaluatorException(name);
        }
        evaluators.put(name, evaluator);
    }

}
