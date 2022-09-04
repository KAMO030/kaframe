package com.kamo.context;

import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface BeanDefinitionRegistry  {


    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeanDefinitionStoreException;


    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    boolean containsBeanDefinition(String beanName);


    String[] getBeanDefinitionNames();


    int getBeanDefinitionCount();

    Map<Class, Object> getFactoryBeans();


    boolean isSingletonCurrentlyInitialized(String beanName);

}