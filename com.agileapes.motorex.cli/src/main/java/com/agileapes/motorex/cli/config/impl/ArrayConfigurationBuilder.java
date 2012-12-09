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
import com.agileapes.motorex.cli.config.ConfigurationBuilder;
import com.agileapes.motorex.cli.config.Option;
import com.agileapes.motorex.cli.config.Specification;
import com.agileapes.motorex.cli.exception.InvalidConfigurationArgumentException;
import com.agileapes.motorex.cli.exception.MissingRequiredOptionsException;
import com.agileapes.motorex.cli.value.ValueReaderContext;
import com.agileapes.motorex.cli.value.ValueReaderContextAware;

import java.util.HashSet;
import java.util.Set;

/**
 * The ArrayConfigurationBuilder will attempt to put together a configuration
 * setting based on an input String array. This is of particular interest since
 * Java&trade; like many other languages will make the command line arguments
 * available as an array of String values.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:12)
 */
public class ArrayConfigurationBuilder implements ConfigurationBuilder, ValueReaderContextAware {

    private final Specification specification;
    private final String[] array;
    private final int start;
    private ValueReaderContext valueReaderContext;

    public ArrayConfigurationBuilder(Specification specification, String... array) {
        this(specification, 0, array);
    }

    public ArrayConfigurationBuilder(Specification specification, int start, String... array) {
        this.specification = specification;
        this.array = array;
        this.start = start;
    }

    @Override
    public void setValueReaderContext(ValueReaderContext valueReaderContext) {
        this.valueReaderContext = valueReaderContext;
    }

    @Override
    public Configuration build() {
        final StandardConfiguration configuration = new StandardConfiguration(specification);
        configuration.setValueReaderContext(valueReaderContext);
        final Set<Option> required = new HashSet<Option>();
        for (Option option : specification.getOptions()) {
            if (option.required()) {
                required.add(option);
            }
        }
        for (int i = start; i < array.length; i ++) {
            String current = array[i];
            boolean isFlag = (i < array.length - 1 && array[i + 1].startsWith("-")) || i == array.length - 1;
            if (current.startsWith("--")) {
                current = current.substring(2);
                if (isFlag) {
                    configuration.setFlag(current);
                } else {
                    String value = array[++ i];
                    if (value.startsWith("\\-")) {
                        value = value.substring(1);
                    }
                    required.remove(specification.getOption(current));
                    configuration.setValue(current, value);
                }
            } else if (current.startsWith("-")) {
                current = current.substring(1);
                if (current.length() > 1) {
                    for (int j = 0; j < current.length(); j ++) {
                        configuration.setFlag(current.charAt(j));
                    }
                } else if (isFlag) {
                    configuration.setFlag(current.charAt(0));
                } else {
                    String value = array[++ i];
                    if (value.startsWith("\\-")) {
                        value = value.substring(1);
                    }
                    required.remove(specification.getOption(current.charAt(0)));
                    configuration.setValue(current.charAt(0), value);
                }
            } else {
                throw new InvalidConfigurationArgumentException(current);
            }
        }
        if (!required.isEmpty()) {
            final Set<String> missing = new HashSet<String>();
            for (Option option : required) {
                missing.add(option.name());
            }
            throw new MissingRequiredOptionsException(missing);
        }
        return configuration;
    }

}
