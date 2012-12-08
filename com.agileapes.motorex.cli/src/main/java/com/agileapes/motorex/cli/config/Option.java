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

package com.agileapes.motorex.cli.config;

/**
 * An Option is a single parameter for the application.
 * Options come in two flavours, flags and parameters.
 * A flag is simply a boolean value that amounts to {@code false} by default and is
 * set to {@code true} when it is presented as an available option.
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:14)
 */
public interface Option {

    /**
     * @return {@code true} means that this option is a flag
     */
    boolean isFlag();

    /**
     * @return the name of the option
     */
    String name();

    /**
     * @return the shorthand for this option (a single character) or {@code null} if this
     * option does not have a short form
     */
    Character shorthand();

    /**
     * @return a human readable description for this option that can be used by the command
     * line user interface to display useful help messages
     */
    String description();

    /**
     * @return the actual, target type of the parameter. Can be anything that has a
     * {@link com.agileapes.motorex.cli.value.ValueReader} assigned as handler
     */
    Class<?> type();

    /**
     * @return {@code true} means that this option must not be omitted. This property
     * is not applicable to flag options.
     */
    boolean required();

}
