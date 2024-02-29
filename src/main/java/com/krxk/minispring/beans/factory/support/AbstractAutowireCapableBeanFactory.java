package com.krxk.minispring.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.PropertyValue;
import com.krxk.minispring.beans.PropertyValues;
import com.krxk.minispring.beans.factory.*;
import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.config.BeanPostProcessor;
import com.krxk.minispring.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// 实现 bean 对象的 单例管理
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean;
        try {
            // 创建对象
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        if (beanDefinition.isSingleton()) {
            addSingleton(beanName, bean);
        }

        return bean;
    }
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declareConstructors = beanClass.getDeclaredConstructors();
        for (Constructor<?> ctor : declareConstructors) {
            if (null != args && ctor.getParameterTypes().length == args.length) { // 此处仅根据参数长度判断使用哪个构造函数
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                // 考虑属性为对象情况
                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName()); // 根据依赖关系递归注入
                }
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (null == current) {
                return  result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (null == current) {
                return  result;
            }
            result = current;
        }
        return result;
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // invokeAwareMethods
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware beanFactoryAware) {
                beanFactoryAware.setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware beanClassLoaderAware) {
                beanClassLoaderAware.setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware beanNameAware) {
                beanNameAware.setBeanName(beanName);
            }
        }


        // 执行 BeanPostProcessor Before 处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        // 待完成内容：invokeInitMethods(beanName, wrappedBean, beanDefinition)
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        }catch (Exception e) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
        }
        // 执行 BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition)
            throws Exception{
        // 实现接口 InitializingBean
        if (wrappedBean instanceof InitializingBean initializingBean) { // JDK 17 支持的模式变量特性
            initializingBean.afterPropertiesSet();
        }

        // 配置 信息 init-method {判断是为了避免二次执行销毁}
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if (null == initMethod) {
                throw new BeansException("Could not find an init method named '" +
                        initMethodName + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(wrappedBean);
        }
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (!beanDefinition.isSingleton()) {
            // 非 单例模式 不执行销毁方法
            return;
        }
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
}
