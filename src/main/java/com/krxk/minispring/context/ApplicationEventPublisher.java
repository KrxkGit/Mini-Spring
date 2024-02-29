package com.krxk.minispring.context;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
