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

import com.agileapes.motorex.cli.exception.UnsupportedValueTypeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Random;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 18:38)
 */
public class CommonTypesValueReaderTest {

    @Test
    public void testInteger() throws Exception {
        final Integer value = new Random().nextInt();
        final Integer read = new CommonTypesValueReader().read(String.valueOf(value), Integer.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testShort() throws Exception {
        final Short value = ((Integer) new Random().nextInt()).shortValue();
        final Short read = new CommonTypesValueReader().read(value.toString(), Short.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testLong() throws Exception {
        final Long value = new Random().nextLong();
        final Long read = new CommonTypesValueReader().read(value.toString(), Long.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testBoolean() throws Exception {
        final Boolean value = new Random().nextBoolean();
        final Boolean read = new CommonTypesValueReader().read(value.toString(), Boolean.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testByte() throws Exception {
        final Byte value = ((Integer) new Random().nextInt()).byteValue();
        final Byte read = new CommonTypesValueReader().read(value.toString(), Byte.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testDouble() throws Exception {
        final Double value = new Random().nextDouble();
        final Double read = new CommonTypesValueReader().read(value.toString(), Double.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testFloat() throws Exception {
        final Float value = new Random().nextFloat();
        final Float read = new CommonTypesValueReader().read(value.toString(), Float.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testString() throws Exception {
        final byte[] bytes = new byte[100 + new Random().nextInt(100)];
        new Random().nextBytes(bytes);
        final String value = new String(bytes);
        System.out.println("value = " + value);
        final String read = new CommonTypesValueReader().read(value, String.class);
        Assert.assertEquals(read, value);
    }

    @Test
    public void testCharacter() throws Exception {
        final Character value = ((char) new Random().nextInt());
        final Character read = new CommonTypesValueReader().read(value.toString(), Character.class);
        Assert.assertEquals(read, value);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testEmptyChar() throws Exception {
        new CommonTypesValueReader().read("", Character.class);
    }

    @Test(expectedExceptions = UnsupportedValueTypeException.class)
    public void testUnsupportedType() throws Exception {
        new CommonTypesValueReader().read("", Date.class);
    }

}
