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

import com.agileapes.motorex.tree.Node;

import java.util.List;

/**
 * This evaluator will look for the index number of the node
 * Signature: index(number : Integer)
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 2:46)
 */
public class IndexNodeEvaluator extends NodeEvaluatorSupport {

    @Override
    public boolean test(Node node, List<String> parameters) {
        require(parameters, 1);
        permit(parameters, 1);
        try {
            final int index = Integer.parseInt(parameters.get(0));
            return index == node.getIndex();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer index: " + parameters.get(0));
        }
    }

}
