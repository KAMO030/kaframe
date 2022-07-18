package com.kamo.context;

import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;

public interface BeanDefinitionRegistry  {


    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeanDefinitionStoreException;

    /**
     * Remove the BeanDefinition for the given name.
     * @param beanName the name of the bean instance to register
     * @throws NoSuchBeanDefinitionException if there is no such bean definition
     */
    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;


    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * Check if this registry contains a bean definition with the given name.
     * @param beanName the name of the bean to look for
     * @return if this registry contains a bean definition with the given name
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Return the names of all beans defined in this registry.
     * @return the names of all beans defined in this registry,
     * or an empty array if none defined
     */
    String[] getBeanDefinitionNames();

    /**
     * Return the number of beans defined in the registry.
     * @return the number of beans defined in the registry
     */
    int getBeanDefinitionCount();

    /**
     * Determine whether the given bean name is already in use within this registry,
     * i.e. whether there is a local bean or alias registered under this name.
     * @param beanName the name to check
     * @return whether the given bean name is already in use
     */
    boolean isBeanNameInUse(String beanName);


}