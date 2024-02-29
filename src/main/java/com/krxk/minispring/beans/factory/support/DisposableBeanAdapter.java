package com.krxk.minispring.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.factory.DisposableBean;
import com.krxk.minispring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {
    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        // 实现接口 DisposableBean
        if (bean instanceof DisposableBean disposableBean) {
            disposableBean.destroy();
        }
        // 配置信息 destroy-method {判断是为了避免二次执行销毁}
        if (StrUtil.isNotEmpty(destroyMethodName)) {
            Method method = bean.getClass().getMethod(destroyMethodName);
            if (method == null) {
                throw new BeansException("Couldn't find a destroy method named '" +
                        destroyMethodName + "' on bean with name '" + beanName + "'");
            }
            method.invoke(bean);
        }
    }
}
