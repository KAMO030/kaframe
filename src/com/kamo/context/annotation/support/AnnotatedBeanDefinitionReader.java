package com.kamo.context.annotation.support;

import com.kamo.bean.support.BeanDefinitionBuilder;
import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.BeanDefinitionRegistry;

import java.beans.Introspector;

public class AnnotatedBeanDefinitionReader {
    private final BeanDefinitionRegistry registry;
    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }
    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerBean(componentClass);
        }
    }

    private void registerBean(Class<?> beanClass) {
        doRegisterBean(beanClass);
    }

    private void doRegisterBean(Class<?> beanClass) {
        BeanDefinition bd = BeanDefinitionBuilder.getBeanDefinition(beanClass);
        try {
            registry.registerBeanDefinition(Introspector.decapitalize(beanClass.getSimpleName()),bd);
        } catch (BeanDefinitionStoreException e) {
            throw new RuntimeException(e);
        }
    }

}
