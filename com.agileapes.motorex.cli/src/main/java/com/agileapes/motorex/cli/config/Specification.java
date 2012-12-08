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

import java.util.Set;

/**
 * A Specification holds information regarding how a single execution
 * target can be configured to run and exposes all of its available
 * options and functionality.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:12)
 */
public interface Specification {

    /**
     * Looks up an Option definition by its full name
     * @param name    the full name
     * @return the option definition
     * @throws NoSuchOptionException
     */
    Option getOption(String name) throws NoSuchOptionException;

    /**
     * Looks up an Option by its shorter name, if available
     * @param shorthand    the short name
     * @return the option definition
     * @throws NoSuchOptionException
     */
    Option getOption(Character shorthand) throws NoSuchOptionException;

    /**
     * Exposes a set of all available options to this target
     * @return a set of options
     */
    Set<Option> getOptions();

}
