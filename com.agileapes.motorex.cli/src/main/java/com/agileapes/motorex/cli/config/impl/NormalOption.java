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

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:22)
 */
public class NormalOption implements Option {

    private final Class<?> type;
    private final String description;
    private final Character shorthand;
    private final String name;
    private final boolean required;

    public NormalOption(String name, Class<?> type, boolean required) {
        this(name, type, required, null);
    }

    public NormalOption(String name, Class<?> type, boolean required, Character shorthand) {
        this(name, type, required, shorthand, "");
    }

    public NormalOption(String name, Class<?> type, boolean required, Character shorthand, String description) {
        this.type = type;
        this.required = required;
        this.description = description;
        this.shorthand = shorthand;
        this.name = name;
    }

    @Override
    public boolean isFlag() {
        return false;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Character shorthand() {
        return shorthand;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public boolean required() {
        return required;
    }

}
