package com.krxk.minispring.beans.factory.support;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.FactoryBean;
import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.config.BeanPostProcessor;
import com.krxk.minispring.beans.factory.config.ConfigurableBeanFactory;
import com.krxk.minispring.utils.ClassUtils;

import java.util.ArrayList;
import java.util.List;

// 实现 Bean 工厂接口
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {
    private final List<BeanPostProcessor>beanPostProcessors = new ArrayList<BeanPostProcessor>();
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    protected <T> T doGetBean(final String name, final Object[] args) {
        Object sharedInstance = getSingleton(name);
        if (sharedInstance != null) {
            return (T) getObjectForBeanInstance(sharedInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition, args);
        return (T) getObjectForBeanInstance(bean, name);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }
        Object object = getCachedObjectForFactoryBean(beanName);
        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }
        return object;
    }

    /**
     * 返回将要被应用的 BeanPostProcessors 列表
     * @return
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    protected abstract BeanDefinition getBeanDefinition(String beansName) throws BeansException;
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args)
            throws BeansException;

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
}
