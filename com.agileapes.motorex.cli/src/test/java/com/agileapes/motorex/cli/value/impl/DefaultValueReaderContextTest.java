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

import com.agileapes.motorex.cli.exception.NoSuchValueReaderException;
import com.agileapes.motorex.cli.exception.TypeAlreadyHandledException;
import com.agileapes.motorex.cli.exception.UnsupportedValueTypeException;
import com.agileapes.motorex.cli.value.ValueReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/9, 2:53)
 */
public class DefaultValueReaderContextTest {

    @Test
    public void testSuccessfulRegistration() throws Exception {
        final DefaultValueReaderContext context = new DefaultValueReaderContext();
        boolean knows = false;
        try {
            context.getValueReader(Integer.class);
            knows = true;
        } catch (NoSuchValueReaderException e) {
        }
        if (knows) {
            Assert.fail();
        }
        context.register(new CommonTypesValueReader());
        final ValueReader reader = context.getValueReader(Integer.class);
        Assert.assertNotNull(reader);
        final Integer integer = reader.read("123", Integer.class);
        Assert.assertEquals(integer, (Integer) 123);
    }

    @Test(expectedExceptions = TypeAlreadyHandledException.class)
    public void testRegistrationOfUnmappedDuplicate() throws Exception {
        final DefaultValueReaderContext context = new DefaultValueReaderContext();
        context.register(new CommonTypesValueReader());
        context.register(new ValueReader() {
            @Override
            public Set<Class<?>> getTypes() {
                final HashSet<Class<?>> classes = new HashSet<Class<?>>();
                classes.add(Integer.class);
                return classes;
            }

            @Override
            public <T> T read(String text, Class<T> type) throws UnsupportedValueTypeException {
                return null;
            }
        });
    }

    @Test(expectedExceptions = TypeAlreadyHandledException.class)
    public void testRegistrationOfMappedDuplicate() throws Exception {
        final DefaultValueReaderContext context = new DefaultValueReaderContext();
        context.register(new CommonTypesValueReader());
        context.register(new ValueReader() {
            @Override
            public Set<Class<?>> getTypes() {
                final HashSet<Class<?>> classes = new HashSet<Class<?>>();
                classes.add(int.class);
                return classes;
            }

            @Override
            public <T> T read(String text, Class<T> type) throws UnsupportedValueTypeException {
                return null;
            }
        });
    }

    @Test
    public void testValueReaderAwareness() throws Exception {
        final DefaultValueReaderContext context = new DefaultValueReaderContext();
        boolean knows = false;
        try {
            context.getValueReader(Integer.class);
            knows = true;
        } catch (NoSuchValueReaderException e) {
        }
        if (knows) {
            Assert.fail();
        }
        final CommonTypesValueReader reader = new CommonTypesValueReader();
        context.register(reader);
        final ValueReader valueReader = context.getValueReader(Integer.class);
        Assert.assertEquals(valueReader, reader);
    }

    @Test
    public void testReadingDirectly() throws Exception {
        final DefaultValueReaderContext context = new DefaultValueReaderContext();
        boolean knows = false;
        try {
            context.read("123", Integer.class);
            knows = true;
        } catch (NoSuchValueReaderException e) {
        }
        if (knows) {
            Assert.fail();
        }
        context.register(new CommonTypesValueReader());
        final Integer integer = context.read("123", Integer.class);
        Assert.assertEquals(integer, (Integer) 123);
    }

}
