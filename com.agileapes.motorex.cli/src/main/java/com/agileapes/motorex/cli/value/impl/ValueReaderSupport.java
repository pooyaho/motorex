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

import com.agileapes.motorex.cli.value.ValueReader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class acts as a supporting base class for all {@link ValueReader}
 * instances and rids them of the need to rewrite the same code for defining
 * target types.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:38)
 */
public abstract class ValueReaderSupport implements ValueReader {

    private final Set<Class<?>> types = new HashSet<Class<?>>();

    protected ValueReaderSupport(Class... types) {
        //noinspection unchecked
        Collections.addAll(this.types, types);
    }

    @Override
    public Set<Class<?>> getTypes() {
        return types;
    }

}
