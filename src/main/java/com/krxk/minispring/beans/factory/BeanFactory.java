package com.krxk.minispring.beans.factory;

import com.krxk.minispring.beans.BeansException;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;
}
