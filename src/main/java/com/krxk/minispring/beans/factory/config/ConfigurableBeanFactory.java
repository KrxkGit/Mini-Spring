package com.krxk.minispring.beans.factory.config;

import com.krxk.minispring.beans.factory.ListableBeanFactory;

public interface ConfigurableBeanFactory extends ListableBeanFactory,
        AutowireCapableBeanFactory, SingletonBeanRegistry {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
