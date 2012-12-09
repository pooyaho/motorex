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
import com.agileapes.motorex.cli.exception.InvalidConfigurationArgumentException;
import com.agileapes.motorex.cli.exception.MissingRequiredOptionsException;
import com.agileapes.motorex.cli.value.impl.CommonTypesValueReader;
import com.agileapes.motorex.cli.value.impl.DefaultValueReaderContext;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/9, 2:23)
 */
public class ArrayConfigurationBuilderTest {

    private StandardSpecification getSpecification() {
        final StandardSpecification specification = new StandardSpecification();
        specification.addOption(new NormalOption("first", Integer.class, false, 'f'));
        specification.addOption(new NormalOption("second", Double.class, true, 's'));
        specification.addOption(new NormalOption("three", String.class, true, 't'));
        specification.addOption(new NormalFlag("fourth", '4'));
        specification.addOption(new NormalFlag("fifth", '5'));
        specification.addOption(new NormalFlag("sixth", '6'));
        specification.addOption(new NormalFlag("seventh", '7'));
        return specification;
    }

    private Configuration build(String ... array) {
        final DefaultValueReaderContext valueReaderContext = new DefaultValueReaderContext();
        valueReaderContext.register(new CommonTypesValueReader());
        final ArrayConfigurationBuilder builder = new ArrayConfigurationBuilder(getSpecification(), array);
        builder.setValueReaderContext(valueReaderContext);
        return builder.build();
    }

    @Test
    public void testBuild() throws Exception {
        final Configuration configuration = build("-45", "-7", "-s", "\\-987", "--three", "\\-Hello", "--sixth");
        Assert.assertNotNull(configuration);
        Assert.assertTrue(configuration.isSet("fourth"));
        Assert.assertTrue(configuration.isSet("fifth"));
        Assert.assertTrue(configuration.isSet("sixth"));
        Assert.assertTrue(configuration.isSet("seventh"));
        Assert.assertEquals(configuration.getOption("second", 123d), -987d);
        Assert.assertEquals(configuration.getOption("three", "hi"), "-Hello");
    }

    @Test(expectedExceptions = InvalidConfigurationArgumentException.class)
    public void testBuildOfInvalidInput() throws Exception {
        build("-45", "hello");
    }

    @Test(expectedExceptions = MissingRequiredOptionsException.class)
    public void testBuildWithMissingRequiredArguments() throws Exception {
        build("-4567", "--first", "123");
    }
}
