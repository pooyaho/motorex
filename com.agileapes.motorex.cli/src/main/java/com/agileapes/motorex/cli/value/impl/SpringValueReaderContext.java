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

package com.agileapes.motorex.cli.value.impl;

import com.agileapes.motorex.cli.value.ValueReader;
import com.agileapes.motorex.cli.value.ValueReaderContextAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2012/12/8, 16:55)
 */
public class SpringValueReaderContext extends DefaultValueReaderContext implements BeanFactoryPostProcessor, BeanPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            final BeanDefinition definition = beanFactory.getBeanDefinition(name);
            if (definition.isSingleton()) {
                final String className = definition.getBeanClassName();
                try {
                    final Class<?> beanType = ClassUtils.forName(className, beanFactory.getBeanClassLoader());
                    if (ValueReader.class.isAssignableFrom(beanType)) {
                        register(beanFactory.getBean(name, ValueReader.class));
                    }
                } catch (ClassNotFoundException e) {
                    throw new BeanCreationNotAllowedException(name, "Failed to access bean");
                }
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ValueReaderContextAware) {
            ((ValueReaderContextAware) bean).setValueReaderContext(this);
        }
        return bean;
    }

}
