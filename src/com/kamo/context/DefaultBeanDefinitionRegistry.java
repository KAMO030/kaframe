package com.kamo.context;

import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry{
    protected final Map<String, BeanDefinition> beanDefinitions;
    protected final Map<Class, Object> factoryBeans;
    public DefaultBeanDefinitionRegistry() {
        this.beanDefinitions = new ConcurrentHashMap<>();
        this.factoryBeans = new ConcurrentHashMap<>();
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        if (!chekBeanName(beanName)) {
            beanDefinitions.put(beanName, beanDefinition);
            Class beanClass = beanDefinition.getBeanClass();
            if (FactoryBean.class.isAssignableFrom(beanClass)) {
                factoryBeans.put(beanClass, beanDefinition);
            }
        } else {
            throw new BeanDefinitionStoreException();
        }
    }
    private boolean chekBeanName(String beanName) {
        return beanDefinitions.containsKey(beanName);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (chekBeanName(beanName)) {
            beanDefinitions.remove(beanName);
        } else {
            throw new NoSuchBeanDefinitionException();
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (chekBeanName(beanName)) {
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
    public boolean isBeanNameInUse(String beanName) {
        return false;
    }
}
