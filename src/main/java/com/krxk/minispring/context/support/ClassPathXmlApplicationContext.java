package com.krxk.minispring.context.support;

import com.krxk.minispring.beans.BeansException;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    private String[] configurations;

    public ClassPathXmlApplicationContext() {
    }

    public ClassPathXmlApplicationContext(String[] configurations) throws BeansException {
        this.configurations = configurations;
        refresh();
    }

    public ClassPathXmlApplicationContext(String configuration) throws BeansException {
        this(new String[]{configuration});
    }

    @Override
    protected String[] getConfigLocations() {
        return configurations;
    }
}
