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

import com.agileapes.motorex.cli.value.TypeMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * This type mapper will map all of Java&trade;'s primitive types into
 * their object-oriented counterparts
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:41)
 */
public class PrimitiveTypeMapper implements TypeMapper {

    private static final Map<String, Class<?>> types = new HashMap<String, Class<?>>();
    static {
        types.put("int", Integer.class);
        types.put("short", Short.class);
        types.put("long", Long.class);
        types.put("boolean", Boolean.class);
        types.put("byte", Byte.class);
        types.put("char", Character.class);
        types.put("double", Double.class);
        types.put("float", Float.class);
    }

    @Override
    public Class<?> getType(Class<?> type) {
        if (type.isPrimitive() && types.containsKey(type.getCanonicalName())) {
            return types.get(type.getCanonicalName());
        }
        return type;
    }

}
