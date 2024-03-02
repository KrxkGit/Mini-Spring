package com.krxk.minispring.beans.factory.config;

import com.krxk.minispring.beans.factory.ListableBeanFactory;
import com.krxk.minispring.utils.StringValueResolver;

public interface ConfigurableBeanFactory extends ListableBeanFactory,
        AutowireCapableBeanFactory, SingletonBeanRegistry {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    /**
     * Add a String resolver for embedded values such as annotation attributes.
     * @param valueResolver the String resolver to apply to embedded values
     * @since 3.0
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * Resolve the given embedded value, e.g. an annotation attribute.
     * @param value the value to resolve
     * @return the resolved value (maybe the original value as-is)
     * @since 3.0
     */
    String resolveEmbeddedValue(String value);
}
