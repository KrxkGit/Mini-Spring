package com.krxk.minispring.beans.factory.support;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.config.BeanDefinition;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean;
        try {
            bean = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
                 | InvocationTargetException e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
        return bean;
    }
}
