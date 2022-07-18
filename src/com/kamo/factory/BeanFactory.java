package com.kamo.factory;

import java.util.HashMap;
import java.util.Map;

public interface BeanFactory {
    Map beanMap = new HashMap();
    boolean	containsBean(String name);
    String[]	getAliases(String name);
    <T> T	getBean(Class<T> requiredType);
    <T> T	getBean(Class<T> requiredType, Object... args);
    Object	getBean(String name);
    <T> T	getBean(String name, Class<T> requiredType);
    Object	getBean(String name, Object... args);
    Class<?>	getType(String name);
    Class<?>	getType(String name, boolean allowFactoryBeanInit);
    boolean	isPrototype(String name);
    boolean	isSingleton(String name);
    boolean	isTypeMatch(String name, Class<?> typeToMatch);
    Object[] getBeans();
}
