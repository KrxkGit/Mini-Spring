package com.krxk.minispring.test.beans;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.*;
import com.krxk.minispring.beans.factory.annotation.Autowired;
import com.krxk.minispring.beans.factory.annotation.Value;
import com.krxk.minispring.context.ApplicationContext;
import com.krxk.minispring.context.ApplicationContextAware;
import com.krxk.minispring.stereotype.Component;

import java.util.Random;

@Component("userService")
public class UserService implements IUserService, InitializingBean, DisposableBean, BeanNameAware,
        ApplicationContextAware, BeanFactoryAware {
    private String uid;
    private String company;
    @Autowired
    private UserDao userDao;
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;
    @Value("${token}")
    private String token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void queryUserInfo() {
//        System.out.println("查询用户信息 " + uid + " 公司:" +company + " " + userDao.queryUserName(uid));
//        System.out.println("查询用户信息 " + uid + " 公司:" +company + " UserDao: " );
        System.out.println("查询用户信息 " + uid + " 公司:" +company + " UserDao: " + userDao.queryUserName("1"));
    }

    @Override
    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("执行：UserService.destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("执行：UserService.afterPropertiesSet");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean Name is：" + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
