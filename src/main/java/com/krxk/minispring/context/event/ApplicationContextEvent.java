package com.krxk.minispring.context.event;

import com.krxk.minispring.context.ApplicationContext;
import com.krxk.minispring.context.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {
    public ApplicationContextEvent(Object source) {
        super(source);
    }
    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
