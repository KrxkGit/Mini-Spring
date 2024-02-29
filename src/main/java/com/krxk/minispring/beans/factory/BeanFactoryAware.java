package com.krxk.minispring.beans.factory;

import com.krxk.minispring.beans.BeansException;

public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
