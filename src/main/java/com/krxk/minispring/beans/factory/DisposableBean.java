package com.krxk.minispring.beans.factory;

public interface DisposableBean {
    /**
     * Bean 销毁时需要进行的操作
     * @throws Exception
     */
    void destroy() throws Exception;
}
