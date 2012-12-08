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

package com.agileapes.motorex.cli.config.impl;

import com.agileapes.motorex.cli.config.Option;
import com.agileapes.motorex.cli.config.Specification;
import com.agileapes.motorex.cli.exception.AmbiguousOptionDefinitionException;
import com.agileapes.motorex.cli.exception.DuplicateOptionException;
import com.agileapes.motorex.cli.exception.NoSuchOptionException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a standard implementation of an Option Specification and is
 * designed in a way that building specifications is typically rather easy.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:25)
 */
public class StandardSpecification implements Specification {

    private final Set<Option> options;

    StandardSpecification() {
        options = new HashSet<Option>();
    }

    void addOption(Option option) {
        for (Option item : options) {
            if (item.name().equals(option.name())) {
                throw new DuplicateOptionException(option.name());
            }
            if (item.shorthand() != null && item.shorthand().equals(option.shorthand())) {
                throw new AmbiguousOptionDefinitionException(item.shorthand());
            }
        }
        options.add(option);
    }

    @Override
    public Option getOption(String name) throws NoSuchOptionException {
        for (Option option : options) {
            if (option.name().equals(name)) {
                return option;
            }
        }
        throw new NoSuchOptionException(name);
    }

    @Override
    public Option getOption(Character shorthand) throws NoSuchOptionException {
        for (Option option : options) {
            if (option.shorthand() != null && option.shorthand().equals(shorthand)) {
                return option;
            }
        }
        throw new NoSuchOptionException(String.valueOf(shorthand));
    }

    @Override
    public Set<Option> getOptions() {
        return Collections.unmodifiableSet(options);
    }

}
