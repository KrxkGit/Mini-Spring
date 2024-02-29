package com.krxk.minispring.context.event;

import com.krxk.minispring.context.ApplicationEvent;
import com.krxk.minispring.context.ApplicationListener;

public interface ApplicationEventMulticaster {
    void addApplicationListener(ApplicationListener<?> listener);


    void removeApplicationListener(ApplicationListener<?> listener);


    void multicastEvent(ApplicationEvent event);
}
