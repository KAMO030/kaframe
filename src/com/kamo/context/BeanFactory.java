package com.kamo.context;

import java.util.List;

public interface BeanFactory {
    boolean containsBeanDefinition(String name);

    boolean containSingletonBean(String name);



    boolean containsBeanDefinition(Class type);

    String[]	getAliases(String name);
    <T> T	getBean(Class<T> requiredType);

    String[] getBeanNamesByType(Class requiredType);

    <T> T	getBean(Class<T> requiredType, Object... args);
    boolean isInUse(Class type);
    void addSingletonBeans(Object...beans);
    Object getInUseBean(String beanName, Class type);

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
