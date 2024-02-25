package com.krxk.minispring.beans;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;
}
