package com.krxk.minispring.beans.factory;

public interface InitializingBean {
    /**
     * Bean 属性填充后添加调用
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
