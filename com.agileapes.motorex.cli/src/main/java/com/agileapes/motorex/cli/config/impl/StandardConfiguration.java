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

import com.agileapes.motorex.cli.config.Configuration;
import com.agileapes.motorex.cli.config.Option;
import com.agileapes.motorex.cli.config.Specification;
import com.agileapes.motorex.cli.exception.InvalidOptionTypeException;
import com.agileapes.motorex.cli.exception.NoSuchOptionException;
import com.agileapes.motorex.cli.value.ValueReaderContext;
import com.agileapes.motorex.cli.value.ValueReaderContextAware;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:18)
 */
public class StandardConfiguration implements Configuration, ValueReaderContextAware {

    private final Specification specification;
    private final Map<Option, String> values;
    private final Set<Option> flags;
    private ValueReaderContext valueReaderContext;

    StandardConfiguration(Specification specification) {
        this.specification = specification;
        this.values = new HashMap<Option, String>();
        this.flags = new HashSet<Option>();
    }

    private void setOptionValue(Option option, String value) {
        if (option.isFlag()) {
            throw new InvalidOptionTypeException(option.name(), true);
        }
        values.put(option, value);
    }

    void setValue(String option, String value) {
        setOptionValue(specification.getOption(option), value);
    }

    void setValue(Character option, String value) {
        setOptionValue(specification.getOption(option), value);
    }

    private void setFlagValue(Option option) {
        if (!option.isFlag()) {
            throw new InvalidOptionTypeException(option.name(), false);
        }
        flags.add(option);
    }

    void setFlag(String flag) {
        setFlagValue(specification.getOption(flag));
    }

    void setFlag(Character flag) {
        setFlagValue(specification.getOption(flag));
    }

    private <T> T getValue(T defaultValue, Option option) {
        if (option.isFlag()) {
            throw new InvalidOptionTypeException(option.name(), true);
        }
        if (!values.containsKey(option)) {
            return defaultValue;
        }
        final String string = values.get(option);
        //noinspection unchecked
        return (T) valueReaderContext.read(string, option.type());
    }

    @Override
    public <T> T getOption(String name, T defaultValue) throws NoSuchOptionException {
        return getValue(defaultValue, specification.getOption(name));
    }

    @Override
    public <T> T getOption(Character shorthand, T defaultValue) throws NoSuchOptionException {
        return getValue(defaultValue, specification.getOption(shorthand));
    }

    @Override
    public boolean hasOption(String name) {
        try {
            specification.getOption(name);
        } catch (NoSuchOptionException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasOption(Character shorthand) {
        try {
            specification.getOption(shorthand);
        } catch (NoSuchOptionException e) {
            return false;
        }
        return true;
    }

    private boolean findFlag(Option option) {
        if (!option.isFlag()) {
            throw new InvalidOptionTypeException(option.name(), false);
        }
        return flags.contains(option);
    }

    @Override
    public boolean isSet(String flag) {
        return findFlag(specification.getOption(flag));
    }

    @Override
    public boolean isSet(Character shorthand) {
        return findFlag(specification.getOption(shorthand));
    }

    @Override
    public void setValueReaderContext(ValueReaderContext valueReaderContext) {
        this.valueReaderContext = valueReaderContext;
    }

}
