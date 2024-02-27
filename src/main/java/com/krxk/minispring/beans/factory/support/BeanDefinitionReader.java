package com.krxk.minispring.beans.factory.support;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.core.io.Resource;
import com.krxk.minispring.core.io.ResourceLoader;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();
    ResourceLoader getResourceLoader();
    void loadBeanDefinitions(Resource resource) throws BeansException, ClassNotFoundException;
    void loadBeanDefinitions(Resource... resources) throws BeansException;
    void loadBeanDefinitions(String location) throws BeansException;
    void loadBeanDefinitions(String... locations) throws BeansException;
}
