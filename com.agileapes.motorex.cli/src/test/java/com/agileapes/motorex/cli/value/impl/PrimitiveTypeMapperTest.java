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

package com.agileapes.motorex.cli.value.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 20:14)
 */
public class PrimitiveTypeMapperTest {

    @Test
    public void testTypeMapping() throws Exception {
        final PrimitiveTypeMapper mapper = new PrimitiveTypeMapper();
        Assert.assertEquals(mapper.getType(int.class), Integer.class);
        Assert.assertEquals(mapper.getType(short.class), Short.class);
        Assert.assertEquals(mapper.getType(long.class), Long.class);
        Assert.assertEquals(mapper.getType(boolean.class), Boolean.class);
        Assert.assertEquals(mapper.getType(byte.class), Byte.class);
        Assert.assertEquals(mapper.getType(char.class), Character.class);
        Assert.assertEquals(mapper.getType(double.class), Double.class);
        Assert.assertEquals(mapper.getType(float.class), Float.class);
    }

}
