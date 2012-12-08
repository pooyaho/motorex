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

package com.agileapes.motorex.tree.description;

import com.agileapes.motorex.tree.Node;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 3:42)
 */
public class NodeMatcher {

    private final NodeDescription description;
    private final Node node;

    NodeMatcher(NodeDescription description, Node node) {
        this.description = description;
        this.node = node;
    }

    public boolean matches() {
        for (EvaluationDescription evaluator : description.getEvaluators()) {
            if (!evaluator.test(node)) {
                return false;
            }
        }
        return true;
    }

}
