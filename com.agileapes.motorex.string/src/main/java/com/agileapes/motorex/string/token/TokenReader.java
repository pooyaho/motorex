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

package com.agileapes.motorex.string.token;

import com.agileapes.motorex.string.exception.TokenizerException;
import com.agileapes.motorex.string.scan.DocumentScanner;

import java.io.Reader;

/**
 * A token reader will take input from the given reader, and return a token definition
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/7, 18:30)
 */
public interface TokenReader {

    /**
     * This method is expected to take data from the {@link Reader} parameter, and either
     * designate a valid token, or return {@code null} if no such token could be found
     * @param documentScanner    the reader used by the tokenizer mechanism
     * @return the token
     * @throws TokenizerException if the tokenization fails in any way
     */
    Token read(DocumentScanner documentScanner) throws TokenizerException;

}
