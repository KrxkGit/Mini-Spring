package com.krxk.minispring.context.support;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.krxk.minispring.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);
    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }
}
