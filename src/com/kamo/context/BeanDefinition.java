package com.kamo.context;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public interface BeanDefinition {
    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    Class getBeanClass();
    Object doInstance(Object[] args) throws InvocationTargetException, IllegalAccessException;
    boolean proContains(String name);
    void setArguments(Parameter[] parameters);
    Arguments getArguments(String name);
    void addArguments(String argName,Arguments arg);

    String[] getArgNames();
    void addProperty(Property property);

    Property getProperty(String name);
    Property[] getPropertys();
    void setBeanClass(Class beanClass);

    void setLazyInit(boolean lazyInit);
    boolean isLazyInit();

    void setInitMethodName(String initMethodName);
    String getInitMethodName();
    Class[]  getArgTypes();
    void setDestroyMethodName(String destroyMethodName);
    String getDestroyMethodName();

    String getScope();
    void setScope(String scope);

    boolean isSingleton();
}
