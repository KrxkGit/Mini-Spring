package com.krxk.minispring.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.PropertyValue;
import com.krxk.minispring.beans.PropertyValues;
import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;

// 实现 bean 对象的 单例管理
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean;
        try {
//            bean = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
            // 创建对象
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
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
}
