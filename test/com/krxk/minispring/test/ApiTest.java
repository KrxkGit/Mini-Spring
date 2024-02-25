package com.krxk.minispring.test;

import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.krxk.minispring.test.beans.UserService;
import org.junit.Test;

public class ApiTest {
    @Test
    public void test_BeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);

        beanFactory.registerBeanDefinition("userService", beanDefinition);

        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();

        UserService userService_singleton = (UserService) beanFactory.getBean("userService");
        userService_singleton.queryUserInfo();
    }
}
