package com.krxk.minispring.context.support;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.krxk.minispring.beans.factory.config.BeanFactoryPostProcessor;
import com.krxk.minispring.beans.factory.config.BeanPostProcessor;
import com.krxk.minispring.context.ConfigurableApplicationContext;
import com.krxk.minispring.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext {
    @Override
    public void refresh() throws BeansException {
        // 创建 BeanFactory 并加载 BeanDefinition
        refreshBeanFactory();
        // 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 在 Bean 实例化之前，执行 BeanFactoryPostProcessor(Invoke factory processors registered as beans in the context)
        invokeBeanFactoryPostProcessors(beanFactory);
        // BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);
        // 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();
    }

    protected abstract void refreshBeanFactory() throws BeansException;
    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap =
                beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap =
                beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
