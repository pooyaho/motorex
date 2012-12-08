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
import com.agileapes.motorex.cli.value.TypeMapper;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:41)
 */
public class CommonTypesValueReader extends ValueReaderSupport {

    private final TypeMapper typeMapper;

    public CommonTypesValueReader() {
        super(Integer.class, Short.class, Long.class, Boolean.class, Byte.class, Character.class,
                Double.class, Float.class, String.class);
        typeMapper = new PrimitiveTypeMapper();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(String text, Class<T> type) throws UnsupportedValueTypeException {
        final Class<?> mappedType = typeMapper.getType(type);
        if (Integer.class.equals(mappedType)) {
            return ((T) ((Integer) Integer.parseInt(text)));
        } else if (Short.class.equals(mappedType)) {
            return ((T) ((Integer) Integer.parseInt(text)));
        } else if (Long.class.equals(mappedType)) {
            return ((T) ((Long) Long.parseLong(text)));
        } else if (Boolean.class.equals(mappedType)) {
            return ((T) ((Boolean) Boolean.parseBoolean(text)));
        } else if (Byte.class.equals(mappedType)) {
            return ((T) ((Byte) Byte.parseByte(text)));
        } else if (Double.class.equals(mappedType)) {
            return ((T) ((Double) Double.parseDouble(text)));
        } else if (Float.class.equals(mappedType)) {
            return ((T) ((Float) Float.parseFloat(text)));
        } else if (String.class.equals(mappedType)) {
            return (T) text;
        } else if (Character.class.equals(mappedType)) {
            if (text.isEmpty()) {
                throw new IllegalArgumentException();
            }
            return ((T) ((Character) text.charAt(0)));
        }
        throw new UnsupportedValueTypeException(type);
    }

}
