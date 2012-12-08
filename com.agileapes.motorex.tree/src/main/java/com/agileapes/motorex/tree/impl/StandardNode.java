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

package com.agileapes.motorex.tree.impl;

import com.agileapes.motorex.tree.Node;
import com.agileapes.motorex.tree.NodeType;
import com.agileapes.motorex.tree.description.NodeDescription;
import com.agileapes.motorex.tree.exception.NoSuchNodeException;
import com.agileapes.motorex.tree.traverse.NodeTraverseCallback;
import com.agileapes.motorex.tree.traverse.TraverseOrder;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 0:58)
 */
public class StandardNode extends AbstractSearchableNode {

    private String namespace;
    private String name;
    private String value;
    private final Map<String, String> attributes = new HashMap<String, String>();
    private final List<Node> children = new ArrayList<Node>();
    private Node nextSibling;
    private Node previousSibling;
    private Node parent;
    private NodeType type = NodeType.INNER_NODE;
    private int index;

    public StandardNode() {
        this("", "");
    }

    public StandardNode(String name) {
        this(name, "");
    }

    public StandardNode(String name, String namespace) {
        this.setNamespace(namespace);
        this.setName(name);
    }

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getNamespace() {
        return namespace == null ? "" : namespace;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null ? "" : name;
    }

    @Override
    public String getQualifiedName() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getNamespace());
        if (!getNamespace().isEmpty()) {
            builder.append(":");
        }
        builder.append(getName());
        return builder.toString();
    }

    @Override
    public void setAttribute(String name, String value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public String getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value == null ? "" : value;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }

    private void fixChildren() {
        for (int i = 0; i < children.size(); i++) {
            final Node node = children.get(i);
            node.setIndex(i);
            node.setParent(this);
            if (i == 0) {
                node.setPreviousSibling(null);
            } else {
                node.setPreviousSibling(children.get(i - 1));
            }
            if (i == children.size() - 1) {
                node.setNextSibling(null);
            } else {
                node.setNextSibling(children.get(i + 1));
            }
        }
    }

    @Override
    public void appendChild(Node child) {
        children.add(child);
        fixChildren();
    }

    @Override
    public void insertBefore(Node origin, Node child) {
        final int index = children.indexOf(origin);
        if (index == -1) {
            throw new NoSuchNodeException();
        }
        children.add(index, child);
        fixChildren();
    }

    @Override
    public void insertAfter(Node origin, Node child) {
        final int index = children.indexOf(origin);
        if (index == -1) {
            throw new NoSuchNodeException();
        }
        children.add(index + 1, child);
        fixChildren();
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public Node getLastChild() {
        if (hasChildren()) {
            return children.get(children.size() - 1);
        }
        return null;
    }

    @Override
    public Node getFirstChild() {
        if (hasChildren()) {
            return children.get(0);
        }
        return null;
    }

    @Override
    public void removeChild(Node child) {
        if (!children.remove(child)) {
            throw new NoSuchNodeException();
        }
        child.setIndex(-1);
        child.setParent(null);
        fixChildren();
    }

    @Override
    public void setNextSibling(Node nextSibling) {
        this.nextSibling = nextSibling;
    }

    @Override
    public Node getNextSibling() {
        return nextSibling;
    }

    @Override
    public void setPreviousSibling(Node previousSibling) {
        this.previousSibling = previousSibling;
    }

    @Override
    public Node getPreviousSibling() {
        return previousSibling;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public NodeType getType() {
        return type;
    }

    @Override
    public void setType(NodeType type) {
        this.type = type;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public <C extends NodeTraverseCallback> C traverse(C callback) {
        if (callback == null) {
            throw new NullPointerException();
        }
        callback.before(this);
        if (TraverseOrder.DOWN.equals(callback.getTraverseOrder())) {
            for (Node child : children) {
                child.traverse(callback);
            }
        } else if (parent != null) {
            parent.traverse(callback);
        }
        callback.after(this);
        return callback;
    }

    @Override
    public String getPath() {
        return traverse(new PathBuilder()).getPath();
    }

    @Override
    public boolean matches(String description) {
        return NodeDescription.compile(description).matcher(this).matches();
    }

    @Override
    public String toString() {
        return getQualifiedName();
    }

}
