package com.krxk.minispring.context;

import com.krxk.minispring.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{
    /**
     * 刷新容器
     * @throws BeansException
     */
    void refresh() throws BeansException;

    /**
     * 定义注册虚拟机钩子的方法
     */
    void registerShutdownHook();

    /**
     * 手动关闭方法
     */
    void close();
}
