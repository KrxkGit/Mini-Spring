package com.krxk.minispring.test;

import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.krxk.minispring.test.beans.UserService;
import org.junit.Test;

public class ApiTest {
    @Test
    public void test_BeanFactory() {
        // 初始化 Bean 工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 注入 Bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 获取 Bean
        UserService userService = (UserService) beanFactory.getBean("userService", "Krxk");
        userService.queryUserInfo();

//        // 测试单例模式
//        UserService userService_singleton = (UserService) beanFactory.getBean("userService");
//        userService_singleton.queryUserInfo();
    }
}
