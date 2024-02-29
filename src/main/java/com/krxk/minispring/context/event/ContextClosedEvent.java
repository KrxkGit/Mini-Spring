package com.krxk.minispring.context.event;

import com.krxk.minispring.context.ApplicationEvent;

public class ContextClosedEvent extends ApplicationEvent {
    public ContextClosedEvent(Object source) {
        super(source);
    }

}
