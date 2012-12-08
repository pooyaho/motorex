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

package com.agileapes.motorex.tree;

import com.agileapes.motorex.tree.traverse.NodeTraverseCallback;
import com.agileapes.motorex.tree.traverse.TraverseOrder;

import java.util.List;
import java.util.Set;

/**
 * This interface will hold basic data regarding an inner node of a tree data structure.
 * The tree structure composed this way can have as many children as needed, and is not
 * restricted to a binary representation.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/7, 23:14)
 */
public interface Node {

    /**
     * This is the namespace for the given node. Namespaces can be used in a variety of ways to
     * help distinguish handlers for the given node.
     * @param namespace    the new namespace
     */
    void setNamespace(String namespace);

    /**
     * @return current node namespace
     */
    String getNamespace();

    /**
     * @param name    the name of the node
     */
    void setName(String name);

    /**
     * @return the name of the node
     */
    String getName();

    /**
     * @return the qualified name of the node (namespace and name)
     */
    String getQualifiedName();

    /**
     * By setting the attribute value to {@code null}, it is possible to remove that
     * attribute from the node
     * @param name     attribute name
     * @param value    attribute value
     */
    void setAttribute(String name, String value);

    /**
     * @param name    the name of the attribute
     * @return {@code true} if the attribute exists
     */
    boolean hasAttribute(String name);

    /**
     * @param name    the name of the attribute
     * @return the value of the attribute or {@code null} if it has not been set
     */
    String getAttribute(String name);

    /**
     * @return the names of all attributes with defined values
     */
    Set<String> getAttributeNames();

    /**
     * @param value    the value to be assigned to this node
     */
    void setValue(String value);

    /**
     * @return the value assigned to the node, or {@code null} if none
     */
    String getValue();

    /**
     * @return the children of this node
     */
    List<Node> getChildren();

    /**
     * Appends a child to the end of the list of children for this node
     * @param child    the child to be added
     */
    void appendChild(Node child);

    /**
     * Inserts a node before the given child
     * @param origin    the child already existing under current node
     * @param child     the new child to be inserted before the origin child
     */
    void insertBefore(Node origin, Node child);

    /**
     * Inserts a node after the given child
     * @param origin    the child already under current node
     * @param child     the new child to be inserted after the origin child
     */
    void insertAfter(Node origin, Node child);

    /**
     * @return {@code true} if at least one child has been attributed to this node
     */
    boolean hasChildren();

    /**
     * @return the last child held by this node
     */
    Node getLastChild();

    /**
     * @return the first child held by this node
     */
    Node getFirstChild();

    /**
     * @param child    removes the given child
     */
    void removeChild(Node child);

    /**
     * @param nextSibling    the next node sharing the same parent
     */
    void setNextSibling(Node nextSibling);

    /**
     * @return the next node sharing the same parentj
     */
    Node getNextSibling();

    /**
     * @param previousSibling    the previous node sharing the same parent
     */
    void setPreviousSibling(Node previousSibling);

    /**
     * @return the previous node sharing the same parent
     */
    Node getPreviousSibling();

    /**
     * @return the parent of this node or {@code null} if this is the root
     */
    Node getParent();

    /**
     * @param parent    this node's parent
     */
    void setParent(Node parent);

    /**
     * @return type of this node
     */
    NodeType getType();

    /**
     * @param type    the type of this node
     */
    void setType(NodeType type);

    /**
     * @return the index number of this node (starting from zero)
     */
    int getIndex();

    /**
     * @param index    new index for this node
     */
    void setIndex(int index);

    /**
     * Performs a traversal in the indicated order
     * @param callback    the callback to be executed
     * @param <C>         the type of the callback
     * @return the callback itself is returned by this method
     */
    <C extends NodeTraverseCallback> C traverse(C callback);

    /**
     * @return a unique path leading to this exact node at this point in time
     */
    String getPath();

    /**
     * Finds all nodes rooted at this node based on the given query
     * @param query    the query
     * @return a list of nodes found by this query
     */
    List<Node> find(String query);

    /**
     * @param description    the given description
     * @return {@code true} if the description matches this node. Note that whether a description
     * matches a node does not mean that it uniquely identifies that node.
     */
    boolean matches(String description);

}
