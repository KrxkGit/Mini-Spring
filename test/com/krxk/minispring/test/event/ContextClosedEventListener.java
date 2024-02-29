package com.krxk.minispring.test.event;

import com.krxk.minispring.context.ApplicationListener;
import com.krxk.minispring.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + this.getClass().getName());
    }
}
