package com.kamo.context.annotation;

import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.Arguments;
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
        ConfigurationClassBeanDefinition bd = new ConfigurationClassBeanDefinition();
        bd.setBeanClass(beanClass);
        String scope = beanClass.isAnnotationPresent(Scope.class)?
                Scope.SINGLETON.equalsIgnoreCase(beanClass.getAnnotation(Scope.class).value())
                        ?Scope.SINGLETON
                        :Scope.PROTOTYPE
                :Scope.SINGLETON;
        bd.setScope(scope);
        bd.setLazyInit(beanClass.isAnnotationPresent(Lazy.class));
        for (Parameter parameter : beanClass.getConstructors()[0].getParameters()) {
            Arguments arguments = new Arguments(parameter);
            bd.addArguments(arguments.getName(), arguments);
        }


        try {
            AutowiredPropertyResolve propertyResolve = new AutowiredPropertyResolve(bd);
            propertyResolve.parse();
            registry.registerBeanDefinition(Introspector.decapitalize(beanClass.getSimpleName()),bd);
        } catch (BeanDefinitionStoreException e) {
            throw new RuntimeException(e);
        }
    }

}
