package com.krxk.minispring.test;

import com.krxk.minispring.beans.PropertyValue;
import com.krxk.minispring.beans.PropertyValues;
import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.config.BeanReference;
import com.krxk.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.krxk.minispring.test.beans.UserDao;
import com.krxk.minispring.test.beans.UserService;
import org.junit.Test;

public class ApiTest {
    @Test
    public void test_BeanFactory() {
        // 初始化 Bean 工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // UserDao 注册
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        // UserService 设置属性
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uid", "1"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        // UserService 注入 Bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 获取 Bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
