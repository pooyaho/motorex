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

package com.agileapes.motorex.cli.config.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 19:51)
 */
public class NormalFlagTest {

    @Test
    public void testNormalFlag() throws Exception {
        final NormalFlag flag = new NormalFlag("name");
        Assert.assertEquals(flag.name(), "name");
        Assert.assertTrue(flag.isFlag());
        Assert.assertFalse(flag.required());
        Assert.assertNull(flag.shorthand());
        Assert.assertNotNull(flag.description());
        Assert.assertEquals(flag.description(), "");
    }

}
