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

import com.agileapes.motorex.cli.exception.NoSuchValueReaderException;

/**
 * The value reader context holds information regarding value reader capabilities
 * and is designed to be able to hide value conversion processes from the side
 * of the developers
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:30)
 */
public interface ValueReaderContext {

    /**
     * Will return a value reader capable of reading the given data
     * and converting them to the specified type
     * @param type    desired target type
     * @return the value reader
     * @throws NoSuchValueReaderException
     */
    ValueReader getValueReader(Class<?> type) throws NoSuchValueReaderException;

    /**
     * This method will find the value reader and read the data
     * without returning the discovered value reader
     * @param text    the textual data
     * @param type    the desired type
     * @param <T>     type of read data
     * @return read object value
     * @throws NoSuchValueReaderException
     */
    <T> T read(String text, Class<T> type) throws NoSuchValueReaderException;

}
