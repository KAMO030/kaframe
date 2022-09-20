package com.kamo.context.factory;

import com.kamo.bean.BeanDefinition;

import java.util.List;

public interface BeanFactory {
    boolean containsBeanDefinition(String name);

    boolean containSingletonBean(String name);



    boolean containsBeanDefinition(Class type);

    String[]	getAliases(String name);
    <T> T	getBean(Class<T> requiredType);

    String[] getBeanNamesByType(Class requiredType);

    <T> T	getBean(Class<T> requiredType, Object... args);

    void addSingletonBeans(Object...beans);

    <T extends Object> T	getBean(String name);
    <T> T	getBean(String name, Class<T> requiredType);
    Object	getBean(String name, Object... args);
    Class<?>	getType(String name);
    Class<?>	getType(String name, boolean allowFactoryBeanInit);
    boolean	isPrototype(String name);
    boolean	isSingleton(String name);
    boolean	isTypeMatch(String name, Class<?> typeToMatch);
    Object[] getBeans();

    <T> List<T> getBeans(Class<T> requiredType);

    Object getSingletonBean(String beanName, BeanDefinition beanDefinition);

    void addSingletonBean(String name, Object bean);

    void destroy();
}
