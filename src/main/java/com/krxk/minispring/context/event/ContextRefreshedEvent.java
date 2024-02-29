package com.krxk.minispring.context.event;

import com.krxk.minispring.context.ApplicationEvent;

public class ContextRefreshedEvent extends ApplicationEvent {
    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
