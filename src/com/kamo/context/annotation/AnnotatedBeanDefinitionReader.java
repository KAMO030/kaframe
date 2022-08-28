package com.kamo.context.annotation;

import com.kamo.context.Arguments;
import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionBuilder;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.exception.BeanDefinitionStoreException;

import java.beans.Introspector;
import java.lang.reflect.Parameter;

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
//        for (Parameter parameter : beanClass.getConstructors()[0].getParameters()) {
//            Arguments arguments = new Arguments(parameter);
//            bd.addArguments(arguments.getName(), arguments);
//        }
        try {
            registry.registerBeanDefinition(Introspector.decapitalize(beanClass.getSimpleName()),bd);
        } catch (BeanDefinitionStoreException e) {
            throw new RuntimeException(e);
        }
    }

}
