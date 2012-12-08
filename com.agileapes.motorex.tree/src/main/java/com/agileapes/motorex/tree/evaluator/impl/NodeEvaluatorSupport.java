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

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 2:37)
 */
public abstract class NodeEvaluatorSupport implements NodeEvaluator {

    protected void require(List<String> parameters, int count) {
        if (parameters.size() < count) {
            throw new IllegalArgumentException("Expected at least <" + count + "> parameters, while found <" + parameters.size() + ">");
        }
    }

    protected void permit(List<String> parameters, int count) {
        if (parameters.size() > count) {
            throw new IllegalArgumentException("Expected at most <" + count + "> parameters, while found <" + parameters.size() + ">");
        }
    }

    protected String get(List<String> parameters, int index, String defaultValue) {
        if (index < parameters.size()) {
            return parameters.get(index);
        }
        return defaultValue;
    }

}
