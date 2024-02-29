package com.krxk.minispring.context;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
