package com.krxk.minispring.context.support;

import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.krxk.minispring.beans.factory.config.BeanFactoryPostProcessor;
import com.krxk.minispring.beans.factory.config.BeanPostProcessor;
import com.krxk.minispring.context.ApplicationEvent;
import com.krxk.minispring.context.ApplicationListener;
import com.krxk.minispring.context.ConfigurableApplicationContext;
import com.krxk.minispring.context.event.ApplicationEventMulticaster;
import com.krxk.minispring.context.event.ContextClosedEvent;
import com.krxk.minispring.context.event.ContextRefreshedEvent;
import com.krxk.minispring.context.event.SimpleApplicationEventMulticaster;
import com.krxk.minispring.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext {
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    private ApplicationEventMulticaster applicationEventMulticaster;
    @Override
    public void refresh() throws BeansException {
        // 创建 BeanFactory 并加载 BeanDefinition
        refreshBeanFactory();
        // 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 手动添加 ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的 Bean 对象都能感知所属的 ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        // 在 Bean 实例化之前，执行 BeanFactoryPostProcessor(Invoke factory processors registered as beans in the context)
        invokeBeanFactoryPostProcessors(beanFactory);
        // BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);
        // 初始化事件发布者
        initApplicationEventMulticaster();
        // 注册事件监听器
        registerListeners();
        // 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();
        // 发布容器刷新完成事件
        finishRefresh();
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }
    private void registerListeners() {
        Collection<ApplicationListener>applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }
    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
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
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        publishEvent(new ContextClosedEvent(this));
        getBeanFactory().destroySingletons();
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
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
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
