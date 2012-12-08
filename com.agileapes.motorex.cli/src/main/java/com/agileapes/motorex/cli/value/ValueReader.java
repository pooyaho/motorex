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

package com.agileapes.motorex.cli.value;

import com.agileapes.motorex.cli.exception.UnsupportedValueTypeException;

import java.util.Set;

/**
 * A value reader is an object that is capable of transforming textual representation
 * of certain object types to their object counterparts, thus, hiding the process of
 * reading a value from a String object completely.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:29)
 */
public interface ValueReader {

    /**
     * @return the types of objects this value reader can handle
     */
    Set<Class<?>> getTypes();

    /**
     * This method will read a textual value and return an object of the
     * required type that is the equivalent of that value.
     * @param text    the textual representation
     * @param type    the desired target type
     * @param <T>     the type of the object representation
     * @return the actual object represented by the textual data
     * @throws UnsupportedValueTypeException
     */
    <T> T read(String text, Class<T> type) throws UnsupportedValueTypeException;

}
