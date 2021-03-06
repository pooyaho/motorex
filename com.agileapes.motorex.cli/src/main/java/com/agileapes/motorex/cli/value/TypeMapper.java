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

package com.agileapes.motorex.cli.value;

/**
 * A type mapper is used to map the definition of any given type to a predesignated
 * type, based on some design time decision.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 15:41)
 */
public interface TypeMapper {

    /**
     * This method will map the given type into another type, or return the
     * given type itself if no mapping is necessary
     * @param type    the input type
     * @return the mapped type
     */
    Class<?> getType(Class<?> type);

}
