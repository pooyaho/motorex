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

import com.agileapes.motorex.cli.exception.NoSuchOptionException;

/**
 * A Configuration is the representation of the configuration
 * requested by the end-user for this execution target to be
 * executed.
 *
 * This is usually after all errors regarding input types and
 * required parameter availability have been dealt with.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:10)
 */
public interface Configuration {

    /**
     * Will return the value of the given parameter or the
     * default value if a value has not been provided
     * @param name            the full name of the option
     * @param defaultValue    the default value
     * @param <T>             the type of the value
     * @return value of the option
     * @throws NoSuchOptionException
     */
    <T> T getOption(String name, T defaultValue) throws NoSuchOptionException;

    /**
     * Will return the value of the given parameter based
     * on the provided shorthand, or the default value if
     * one has not been provided
     * @param shorthand       the short name of the option
     * @param defaultValue    the default value
     * @param <T>             the type of the value
     * @return value of the option
     * @throws NoSuchOptionException
     */
    <T> T getOption(Character shorthand, T defaultValue) throws NoSuchOptionException;

    /**
     * Will determine whether a value has been provided for the
     * specified option
     * @param name    the name of the option
     * @return {@code true} if a value is present
     */
    boolean hasOption(String name);

    /**
     * Will determine whether a value has been provided for the
     * specified option based on its short name
     * @param shorthand    the short name for the option
     * @return {@code true} if a value is present
     */
    boolean hasOption(Character shorthand);

    /**
     * Will determine whether a given flag is set
     * @param flag    the name of the flag
     * @return {@code true} if the flag has been set
     */
    boolean isSet(String flag);

    /**
     * Will determine whether a given flag is set
     * @param shorthand    the short name of the flag
     * @return {@code true} if the flag has been set
     */
    boolean isSet(Character shorthand);

}
