package com.krxk.minispring.beans.factory.support;

import com.krxk.minispring.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
