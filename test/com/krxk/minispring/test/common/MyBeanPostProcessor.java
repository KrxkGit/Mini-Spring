package com.krxk.minispring.test.common;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.config.BeanPostProcessor;
import com.krxk.minispring.test.beans.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setUid("3");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
