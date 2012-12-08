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

package com.agileapes.motorex.tree.traverse.impl;

import com.agileapes.motorex.tree.Node;
import com.agileapes.motorex.tree.traverse.NodeTraverseCallback;
import com.agileapes.motorex.tree.traverse.TraverseOrder;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 1:47)
 */
public class NodeStringBuilder implements NodeTraverseCallback {

    private final StringBuilder builder = new StringBuilder();

    @Override
    public TraverseOrder getTraverseOrder() {
        return TraverseOrder.DOWN;
    }

    @Override
    public void before(Node node) {
        if (node.getPreviousSibling() != null) {
            builder.append(",");
        }
        builder.append(node.toString());
        if (node.hasChildren()) {
            builder.append("(");
        }
    }

    @Override
    public void after(Node node) {
        if (node.hasChildren()) {
            builder.append(")");
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }

}
