package com.krxk.minispring.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.support.BeanDefinitionRegistry;
import com.krxk.minispring.stereotype.Component;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{
    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidates) {
                // 解析 Bean 的作用域 singleton、prototype
                String beanScope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(beanScope)) {
                    beanDefinition.setScope(beanScope);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition) ,beanDefinition);
            }
        }
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getBeanClass();
        Scope scope = clazz.getAnnotation(Scope.class);
        if (null != scope) {
            return scope.value();
        }
        return StrUtil.EMPTY;
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getBeanClass();
        Component component = clazz.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(clazz.getSimpleName());
        }
        return value;
    }
}
