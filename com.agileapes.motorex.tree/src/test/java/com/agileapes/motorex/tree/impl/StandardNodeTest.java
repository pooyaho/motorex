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

import com.agileapes.motorex.tree.NodeType;
import com.agileapes.motorex.tree.exception.NoSuchNodeException;
import com.agileapes.motorex.tree.traverse.TraverseOrder;
import com.agileapes.motorex.tree.traverse.impl.NodeStringBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 1:09)
 */
public class StandardNodeTest {

    @Test
    public void testSimpleTree() throws Exception {
        final StandardNode node = new StandardNode("+", "o");
        final StandardNode a = new StandardNode("*", "o");
        node.appendChild(a);
        node.appendChild(new StandardNode("a"));
        a.appendChild(new StandardNode("b"));
        a.appendChild(new StandardNode("c"));
        String string = node.traverse(TraverseOrder.DOWN, new NodeStringBuilder()).toString();
        Assert.assertEquals(string, "o:+(o:*(b,c),a)");
        a.insertBefore(a.getFirstChild(), new StandardNode("x"));
        a.insertAfter(a.getFirstChild(), new StandardNode("y"));
        string = node.traverse(TraverseOrder.DOWN, new NodeStringBuilder()).toString();
        Assert.assertEquals(string, "o:+(o:*(x,y,b,c),a)");
        Assert.assertNotNull(node.getType());
        Assert.assertEquals(node.getType(), NodeType.INNER_NODE);
        node.setType(NodeType.IGNORED);
        Assert.assertEquals(node.getType(), NodeType.IGNORED);
    }

    @Test
    public void testAppendChild() throws Exception {
        //initializing the tree
        final StandardNode parent = new StandardNode("a", "x");
        Assert.assertFalse(parent.hasChildren());
        Assert.assertNull(parent.getParent());
        Assert.assertNull(parent.getPreviousSibling());
        Assert.assertNull(parent.getNextSibling());
        Assert.assertEquals(parent.getIndex(), 0);
        //adding the first child
        final StandardNode child = new StandardNode("b");
        parent.appendChild(child);
        Assert.assertTrue(parent.hasChildren());
        Assert.assertEquals(parent.getChildren().size(), 1);
        Assert.assertEquals(parent.getChildren().get(0).getName(), "b");
        Assert.assertNull(child.getPreviousSibling());
        Assert.assertNull(child.getNextSibling());
        Assert.assertEquals(child.getIndex(), 0);
        Assert.assertNotNull(child.getParent());
        Assert.assertEquals(child.getParent(), parent);
    }

    @Test(expectedExceptions = NoSuchNodeException.class)
    public void testInsertAfter() throws Exception {
        //initializing the tree
        final StandardNode parent = new StandardNode("a", "x");
        Assert.assertFalse(parent.hasChildren());
        Assert.assertNull(parent.getParent());
        Assert.assertNull(parent.getPreviousSibling());
        Assert.assertNull(parent.getNextSibling());
        //adding the first child
        final StandardNode first = new StandardNode("b");
        parent.appendChild(first);
        //adding the second child
        final StandardNode second = new StandardNode("c");
        parent.insertAfter(first, second);
        Assert.assertTrue(parent.hasChildren());
        Assert.assertEquals(parent.getChildren().size(), 2);
        Assert.assertEquals(parent.getChildren().get(0), first);
        Assert.assertEquals(parent.getChildren().get(1), second);
        Assert.assertEquals(second.getPreviousSibling(), first);
        Assert.assertEquals(first.getNextSibling(), second);
        Assert.assertNull(second.getNextSibling());
        Assert.assertNull(first.getPreviousSibling());
        Assert.assertEquals(second.getParent(), parent);
        Assert.assertEquals(first.getIndex(), 0);
        Assert.assertEquals(second.getIndex(), 1);
        //making the erroneous modification
        parent.insertAfter(new StandardNode(), new StandardNode());
    }

    @Test(expectedExceptions = NoSuchNodeException.class)
    public void testInsertBefore() throws Exception {
        //initializing the tree
        final StandardNode parent = new StandardNode("a", "x");
        Assert.assertFalse(parent.hasChildren());
        Assert.assertNull(parent.getParent());
        Assert.assertNull(parent.getPreviousSibling());
        Assert.assertNull(parent.getNextSibling());
        //adding the first child
        final StandardNode first = new StandardNode("b");
        parent.appendChild(first);
        //adding the second child
        final StandardNode second = new StandardNode("c");
        parent.insertBefore(first, second);
        Assert.assertTrue(parent.hasChildren());
        Assert.assertEquals(parent.getChildren().size(), 2);
        Assert.assertEquals(parent.getChildren().get(0), second);
        Assert.assertEquals(parent.getChildren().get(1), first);
        Assert.assertEquals(second.getNextSibling(), first);
        Assert.assertEquals(first.getPreviousSibling(), second);
        Assert.assertNull(second.getPreviousSibling());
        Assert.assertNull(first.getNextSibling());
        Assert.assertEquals(second.getParent(), parent);
        Assert.assertEquals(second.getIndex(), 0);
        Assert.assertEquals(first.getIndex(), 1);
        //making the erroneous modification
        parent.insertBefore(new StandardNode(), new StandardNode());
    }

    @Test(expectedExceptions = NoSuchNodeException.class)
    public void testRemovingChildren() throws Exception {
        final StandardNode parent = new StandardNode("a");
        final StandardNode child = new StandardNode("b");
        parent.appendChild(child);
        Assert.assertTrue(parent.hasChildren());
        Assert.assertEquals(parent.getChildren().size(), 1);
        Assert.assertEquals(child.getParent(), parent);
        Assert.assertEquals(parent.getChildren().get(0), child);
        Assert.assertEquals(child.getIndex(), 0);
        parent.removeChild(child);
        Assert.assertEquals(child.getIndex(), -1);
        Assert.assertNull(child.getParent());
        Assert.assertFalse(parent.hasChildren());
        //the erroneous modification comes here
        parent.removeChild(child);
    }

    @Test
    public void testLastAndFirstChild() throws Exception {
        final StandardNode parent = new StandardNode("a");
        Assert.assertFalse(parent.hasChildren());
        Assert.assertNull(parent.getFirstChild());
        Assert.assertNull(parent.getLastChild());
        final StandardNode first = new StandardNode("b");
        parent.appendChild(first);
        Assert.assertTrue(parent.hasChildren());
        Assert.assertEquals(parent.getFirstChild(), first);
        Assert.assertEquals(parent.getLastChild(), first);
        final StandardNode second = new StandardNode("c");
        parent.appendChild(second);
        Assert.assertEquals(parent.getChildren().size(), 2);
        Assert.assertEquals(parent.getFirstChild(), first);
        Assert.assertEquals(parent.getLastChild(), second);
    }

    @Test
    public void testNodePath() throws Exception {
        final StandardNode a = new StandardNode("a");
        a.appendChild(new StandardNode("b"));
        a.appendChild(new StandardNode("c"));
        final StandardNode d = new StandardNode("d");
        a.appendChild(d);
        d.appendChild(new StandardNode("e"));
        final StandardNode f = new StandardNode("f");
        d.appendChild(f);
        d.appendChild(new StandardNode("g"));
        f.appendChild(new StandardNode("h"));
        final StandardNode i = new StandardNode("i");
        f.appendChild(i);
        final String path = i.getPath();
        Assert.assertEquals(path, "/#0//#2//#1//#1");
    }

    @Test
    public void testNodeValue() throws Exception {
        final StandardNode node = new StandardNode();
        Assert.assertNotNull(node.getValue());
        Assert.assertTrue(node.getValue().isEmpty());
        final String value = "this is a test";
        node.setValue(value);
        Assert.assertEquals(node.getValue(), value);
    }

    @Test
    public void testAttributes() throws Exception {
        final StandardNode node = new StandardNode();
        Assert.assertTrue(node.getAttributeNames().isEmpty());
        Assert.assertFalse(node.hasAttribute("test"));
        node.setAttribute("test", "value");
        Assert.assertFalse(node.getAttributeNames().isEmpty());
        Assert.assertEquals(node.getAttributeNames().size(), 1);
        Assert.assertEquals(node.getAttributeNames().iterator().next(), "test");
        Assert.assertTrue(node.hasAttribute("test"));
        Assert.assertEquals(node.getAttribute("test"), "value");
        node.setAttribute("test", null);
        Assert.assertFalse(node.hasAttribute("test"));
        Assert.assertTrue(node.getAttributeNames().isEmpty());
    }

}
