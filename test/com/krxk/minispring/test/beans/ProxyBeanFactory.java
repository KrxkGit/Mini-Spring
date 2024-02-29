package com.krxk.minispring.test.beans;

import com.krxk.minispring.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class ProxyBeanFactory implements FactoryBean<IUserDao> {
    @Override
    public IUserDao getObject() throws Exception {
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("1", "Krxk-1");
            hashMap.put("2", "Krxk-2");
            hashMap.put("3", "Krxk-3");

            return "你被代理了 " + method.getName() + "：" + hashMap.get(args[0].toString());
        };
        return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class}, invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
