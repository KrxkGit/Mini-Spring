package com.krxk.minispring.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.krxk.minispring.beans.BeansException;
import com.krxk.minispring.beans.PropertyValue;
import com.krxk.minispring.beans.factory.config.BeanDefinition;
import com.krxk.minispring.beans.factory.config.BeanReference;
import com.krxk.minispring.beans.factory.support.AbstractBeanDefinitionReader;
import com.krxk.minispring.beans.factory.support.BeanDefinitionRegistry;
import com.krxk.minispring.context.annotation.ClassPathBeanDefinitionScanner;
import com.krxk.minispring.core.io.Resource;
import com.krxk.minispring.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        super(beanDefinitionRegistry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        super(beanDefinitionRegistry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (IOException | ClassNotFoundException | DocumentException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();

        // 解析 context:component-scan 标签，扫描包中的类并提取相关信息，用于组装 BeanDefinition
        Element componentScan = root.element("component-scan");
        if (null != componentScan) {
            String scanPath = componentScan.attributeValue("base-package");
            if (StrUtil.isEmpty(scanPath)) {
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(scanPath);
        }

        List<Element> beanList = root.elements("bean");
        for (Element bean : beanList) {
            // 解析 Bean 标签
            String id = bean.attributeValue("id");
            String name = bean.attributeValue("name");
            String className = bean.attributeValue("class");
            String beanScope = bean.attributeValue("scope");

            //增加对init-method、destroy-method的读取
            String initMethodName = bean.attributeValue("init-method");
            String destroyMethodName = bean.attributeValue("destroy-method");

            // 获取 Class
            Class<?> clazz = Class.forName(className);
            // 优先级 id > name
            String beanName = StrUtil.isNotEmpty(id)? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            // 定义 bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            // 额外设置到 BeanDefinition 中
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);

            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }


            // 读取属性并填充
            List<Element> propertyList = bean.elements("property");
            for (Element property : propertyList) {
                // 解析 Property
                String attrName = property.attributeValue("name");
                String attrValue = property.attributeValue("value");
                String attrRef = property.attributeValue("ref");
                // 获取属性值，引入对象、值对象
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                // 创建属性信息
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            // 注册 BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
    private void scanPackage(String scanPath) {
        String[] basePackages = StrUtil.splitToArray(scanPath, ',');
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }
}
