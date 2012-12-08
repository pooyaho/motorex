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

import java.util.List;

/**
 * This interface is designed so that an implementation can look at a node and evaluate it,
 * deciding whether the given node is a match or not.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 2:32)
 */
public interface NodeEvaluator {

    /**
     * This method is called to inspect the given node against the set of parameters available
     * to decide whether this node matches the criteria or not.
     * @param node          the node being examined
     * @param parameters    the criteria parameters
     * @return {@code true} would mean that this node should be accepted as matching the criteria
     */
    boolean test(Node node, List<String> parameters);

}
