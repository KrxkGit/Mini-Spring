package com.krxk.minispring.beans.factory;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.config.AutowireCapableBeanFactory;
import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory,
        AutowireCapableBeanFactory, ConfigurableBeanFactory {
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    void preInstantiateSingletons() throws BeansException;
}
