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

import com.agileapes.motorex.tree.Node;
import com.agileapes.motorex.tree.impl.StandardNode;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 2:50)
 */
public abstract class NodeEvaluatorTest {

    protected Node getNode() {
        final StandardNode node = new StandardNode("node");
        node.setAttribute("name", "node");
        node.setAttribute("age", "128");
        node.setAttribute("xyz", "");
        node.appendChild(new StandardNode("a"));
        node.appendChild(new StandardNode("b"));
        node.appendChild(new StandardNode("c"));
        return node;
    }

}
