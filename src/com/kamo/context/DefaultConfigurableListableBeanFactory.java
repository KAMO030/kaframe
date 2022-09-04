package com.kamo.context;

import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.context.factory.BeanFactoryPostProcessor;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.factory.BeanPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.util.Converter;
import com.kamo.util.ConverterRegistry;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultConfigurableListableBeanFactory implements ConfigurableListableBeanFactory {
    protected final List<BeanFactoryPostProcessor> beanFactoryPostProcessors;
    protected final Set<BeanInstanceProcessor> beanInstanceProcessors;
    protected final Set<BeanPostProcessor> beanPostProcessors;

    protected final BeanDefinitionRegistry registry;
    private final BeanFactory beanFactory;

    public DefaultConfigurableListableBeanFactory(BeanFactory beanFactory,BeanDefinitionRegistry registry) {
        this.beanFactoryPostProcessors = new ArrayList<>();
        this.beanInstanceProcessors = new HashSet<>();
        this.beanPostProcessors = new HashSet<>();
        this.registry = registry;
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerConfiguration(String beanName, BeanDefinition beanDefinition) {
        if (!registry.containsBeanDefinition(beanName)) {
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
        if (beanFactory.containSingletonBean(beanName)) {
            return;
        }
        Class beanClass = beanDefinition.getBeanClass();
        if (BeanFactoryPostProcessor.class.isAssignableFrom(beanClass)) {
            beanFactoryPostProcessors.add(beanFactory.getBean(beanName));
        }
        if (BeanInstanceProcessor.class.isAssignableFrom(beanClass)) {
            beanInstanceProcessors.add( beanFactory.getBean(beanName));
        }
        if (BeanPostProcessor.class.isAssignableFrom(beanClass)) {
            beanPostProcessors.add(beanFactory.getBean(beanName));
        }
        if (Converter.class.isAssignableFrom(beanClass)) {
            ConverterRegistry.registerConverter( beanFactory.getBean(beanName));
        }
    }

    @Override
    public BeanDefinition registerConfiguration(Class configClass) {
        register(configClass);
        String configName = Introspector.decapitalize(configClass.getSimpleName());
        BeanDefinition beanDefinition = registry.getBeanDefinition(configName);
        registerConfiguration(configName, beanDefinition);
        return beanDefinition;
    }



    @Override
    public void register(Class... beanClases) {
        for (Class beanClass : beanClases) {
            registerConfiguration(beanClass);
        }
    }


    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return registry.getBeanDefinition(beanName);
    }


    @Override
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }
    @Override
    public Set<BeanInstanceProcessor> getBeanInstanceProcessors() {
        return beanInstanceProcessors;
    }
    @Override
    public Set<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }
}
