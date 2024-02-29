package com.krxk.minispring.context;

import com.krxk.minispring.beans.factory.HierarchicalBeanFactory;
import com.krxk.minispring.beans.factory.ListableBeanFactory;
import com.krxk.minispring.core.io.ResourceLoader;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory,
        ResourceLoader, ApplicationEventPublisher {
}
