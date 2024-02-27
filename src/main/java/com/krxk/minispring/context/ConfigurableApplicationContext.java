package com.krxk.minispring.context;

import com.krxk.minispring.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{
    /**
     * 刷新容器
     * @throws BeansException
     */
    void refresh() throws BeansException;
}
