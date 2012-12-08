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
import com.agileapes.motorex.cli.value.TypeMapper;
import com.agileapes.motorex.cli.value.ValueReader;
import com.agileapes.motorex.cli.value.ValueReaderContext;
import com.agileapes.motorex.cli.value.ValueReaderRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the default value reader context implementation provided by the framework
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:34)
 */
public class DefaultValueReaderContext implements ValueReaderContext, ValueReaderRegistry {

    private final Map<Class<?>, ValueReader> valueReaders;
    private final TypeMapper typeMapper;

    public DefaultValueReaderContext() {
        valueReaders = new HashMap<Class<?>, ValueReader>();
        typeMapper = new PrimitiveTypeMapper();
    }

    @Override
    public ValueReader getValueReader(Class<?> type) throws NoSuchValueReaderException {
        if (!valueReaders.containsKey(type)) {
            throw new NoSuchValueReaderException(type);
        }
        return valueReaders.get(type);
    }

    @Override
    public <T> T read(String text, Class<T> type) throws NoSuchValueReaderException {
        return getValueReader(type).read(text, type);
    }

    @Override
    public void register(ValueReader valueReader) throws TypeAlreadyHandledException {
        for (Class<?> type : valueReader.getTypes()) {
            final Class<?> mapped = typeMapper.getType(type);
            if (valueReaders.containsKey(type)) {
                throw new TypeAlreadyHandledException(type);
            }
            if (valueReaders.containsKey(mapped)) {
                throw new TypeAlreadyHandledException(mapped);
            }
            valueReaders.put(type, valueReader);
            valueReaders.put(mapped, valueReader);
        }
    }

}
