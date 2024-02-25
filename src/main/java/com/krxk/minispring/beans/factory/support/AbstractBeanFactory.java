package com.krxk.minispring.beans.factory.support;

import com.krxk.minispring.beans.BeanFactory;
import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = getSingleton(name);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name, beanDefinition);
    }

    protected abstract BeanDefinition getBeanDefinition(String beansName) throws BeansException;
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;
}
