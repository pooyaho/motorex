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

package com.agileapes.motorex.cli.target;

import com.agileapes.motorex.cli.config.Configuration;
import com.agileapes.motorex.cli.config.Option;

import java.util.List;

/**
 * An ExecutionTarget is an executable class which is independent of the
 * rest of the execution mechanism and will interact with user input only
 * in the vaguest form of definition.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:26)
 */
public interface ExecutionTarget {

    /**
     * @return the unique identifier of this target across the application
     */
    String getIdentifier();

    /**
     * @return the options used and defined by this target
     */
    List<Option> getOptions();

    /**
     * This method is the centerpiece of the execution of each target, and will be
     * called with a verified instance of {@link Configuration}, making the provided
     * options and their values accessible to the execution target itself
     * @param configuration    the configuration of options as provided by the user
     */
    void execute(Configuration configuration);

}
