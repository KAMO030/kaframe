package com.kamo.context.factory;

import com.kamo.bean.BeanDefinition;
import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;

import java.util.Map;

public interface BeanDefinitionRegistry  {


    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeanDefinitionStoreException;
    BeanDefinition registerBeanDefinition(String beanName,Class beanClass);

    BeanDefinition registerBeanDefinition(Class beanClass);
    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    boolean containsBeanDefinition(String beanName);


    String[] getBeanDefinitionNames();


    int getBeanDefinitionCount();

    Map<Class, Object> getFactoryBeans();


    boolean isSingletonCurrentlyInitialized(String beanName);

}