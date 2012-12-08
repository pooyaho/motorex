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

import com.agileapes.motorex.cli.config.Specification;
import com.agileapes.motorex.cli.exception.InvalidOptionTypeException;
import com.agileapes.motorex.cli.exception.NoSuchOptionException;
import com.agileapes.motorex.cli.value.impl.CommonTypesValueReader;
import com.agileapes.motorex.cli.value.impl.DefaultValueReaderContext;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 19:53)
 */
public class StandardConfigurationTest {

    private Specification getSpecification() {
        final StandardSpecification specification = new StandardSpecification();
        specification.addOption(new NormalOption("first", Integer.class, false, 'f'));
        specification.addOption(new NormalOption("second", Double.class, true));
        specification.addOption(new NormalFlag("valid", 'v'));
        return specification;
    }

    private StandardConfiguration getConfiguration() {
        final StandardConfiguration configuration = new StandardConfiguration(getSpecification());
        final DefaultValueReaderContext context = new DefaultValueReaderContext();
        context.register(new CommonTypesValueReader());
        configuration.setValueReaderContext(context);
        return configuration;
    }

    @Test
    public void testHasOptionByName() throws Exception {
        Assert.assertTrue(getConfiguration().hasOption("first"));
        Assert.assertTrue(getConfiguration().hasOption("second"));
        Assert.assertTrue(getConfiguration().hasOption("valid"));
        Assert.assertFalse(getConfiguration().hasOption("third"));
    }

    @Test
    public void testHasOptionByShorthand() throws Exception {
        Assert.assertTrue(getConfiguration().hasOption('f'));
        Assert.assertFalse(getConfiguration().hasOption('t'));
    }

    @Test(expectedExceptions = NoSuchOptionException.class)
    public void testSetValueByNameForNonExistentOption() throws Exception {
        getConfiguration().setValue("third", "");
    }

    @Test(expectedExceptions = NoSuchOptionException.class)
    public void testSetValueByShorthandForNonExistentOption() throws Exception {
        getConfiguration().setValue('t', "");
    }

    @Test
    public void testSetValueByName() throws Exception {
        getConfiguration().setValue("first", "1234");
    }

    @Test
    public void testSetValueByShorthand() throws Exception {
        getConfiguration().setValue('f', "1234");
    }

    @Test
    public void testGetOptionByName() throws Exception {
        final StandardConfiguration configuration = getConfiguration();
        configuration.setValue("first", "123");
        final Integer first = configuration.getOption("first", 456);
        Assert.assertNotNull(first);
        Assert.assertEquals(first, (Integer) 123);
        final Double second = configuration.getOption("second", 456d);
        Assert.assertNotNull(second);
        Assert.assertEquals(second, 456d);
    }

    @Test
    public void testGetOptionByShorthand() throws Exception {
        final StandardConfiguration configuration = getConfiguration();
        Assert.assertEquals(configuration.getOption('f', 456), (Integer) 456);
        configuration.setValue("first", "123");
        Assert.assertEquals(configuration.getOption('f', 456), (Integer) 123);
    }

    @Test(expectedExceptions = InvalidOptionTypeException.class)
    public void testGetOptionForFlag() throws Exception {
        getConfiguration().getOption("valid", false);
    }

    @Test(expectedExceptions = InvalidOptionTypeException.class)
    public void testSetValueForFlag() throws Exception {
        getConfiguration().setValue("valid", "123");
    }

    @Test(expectedExceptions = InvalidOptionTypeException.class)
    public void testSettingNonFlag() throws Exception {
        getConfiguration().setFlag("first");
    }

    @Test
    public void testSettingFlag() throws Exception {
        final StandardConfiguration configuration = getConfiguration();
        Assert.assertFalse(configuration.isSet("valid"));
        configuration.setFlag("valid");
        Assert.assertTrue(configuration.isSet("valid"));
    }

    @Test(expectedExceptions = InvalidOptionTypeException.class)
    public void testIsSetForNonFlag() throws Exception {
        getConfiguration().isSet("first");
    }

    @Test
    public void testIsSetByShortHand() throws Exception {
        final StandardConfiguration configuration = getConfiguration();
        Assert.assertFalse(configuration.isSet('v'));
        configuration.setFlag('v');
        Assert.assertTrue(configuration.isSet('v'));
    }

}
