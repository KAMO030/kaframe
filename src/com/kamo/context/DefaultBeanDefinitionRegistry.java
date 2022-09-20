package com.kamo.context;

import com.kamo.bean.support.AnnotationBeanDefinitionBuilder;
import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.BeanDefinitionRegistry;
import com.kamo.context.factory.FactoryBean;

import java.beans.Introspector;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {
    protected final Map<String, BeanDefinition> beanDefinitions;
    protected final Map<Class, Object> factoryBeans;

    public DefaultBeanDefinitionRegistry() {
        this.beanDefinitions = new ConcurrentHashMap<>();
        this.factoryBeans = new ConcurrentHashMap<>();
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        if (!checkBeanName(beanName)) {
            beanDefinitions.put(beanName, beanDefinition);
            Class beanClass = beanDefinition.getBeanClass();
            if (FactoryBean.class.isAssignableFrom(beanClass)) {
                factoryBeans.put(beanClass, beanDefinition);
            }
        } else {
            throw new BeanDefinitionStoreException();
        }
    }

    @Override
    public BeanDefinition registerBeanDefinition(String beanName, Class beanClass) {
        BeanDefinition beanDefinition = AnnotationBeanDefinitionBuilder.getBeanDefinition(beanClass);
        registerBeanDefinition(beanName, beanDefinition);
        return beanDefinition;
    }

    @Override
    public BeanDefinition registerBeanDefinition(Class beanClass) {
        String beanName = Introspector.decapitalize(beanClass.getSimpleName());
        return registerBeanDefinition(beanName, beanClass);
    }

    private boolean checkBeanName(String beanName) {
        return beanDefinitions.containsKey(beanName);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (checkBeanName(beanName)) {
            beanDefinitions.remove(beanName);
        } else {
            throw new NoSuchBeanDefinitionException();
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (checkBeanName(beanName)) {
            return beanDefinitions.get(beanName);
        } else {
            throw new NoSuchBeanDefinitionException();
        }
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitions.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitions.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitions.size();

    }

    @Override
    public Map<Class, Object> getFactoryBeans() {
        return factoryBeans;
    }

    @Override
    public boolean isSingletonCurrentlyInitialized(String beanName) {
        return false;
    }

}
