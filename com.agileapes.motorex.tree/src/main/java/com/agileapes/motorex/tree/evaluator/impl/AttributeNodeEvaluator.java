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
import java.util.regex.Pattern;

/**
 * This evaluator will look for attribute names and values.
 * Signature: attribute(name : Pattern [, value : Pattern])
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 2:41)
 */
public class AttributeNodeEvaluator extends NodeEvaluatorSupport {

    @Override
    public boolean test(Node node, List<String> parameters) {
        require(parameters, 1);
        permit(parameters, 2);
        final Pattern name = Pattern.compile(parameters.get(0), Pattern.DOTALL);
        final Pattern value = Pattern.compile(get(parameters, 1, ".*"), Pattern.DOTALL);
        for (String attributeName : node.getAttributeNames()) {
            if (!name.matcher(attributeName).matches()) {
                continue;
            }
            if (value.matcher(node.getAttribute(attributeName)).matches()) {
                return true;
            }
        }
        return false;
    }

}
