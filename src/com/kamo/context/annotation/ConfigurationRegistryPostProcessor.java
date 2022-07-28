package com.kamo.context.annotation;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;

import java.util.HashSet;
import java.util.Set;

public class ConfigurationRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    Set<BeanDefinition> configBeanDefinitions;

    public ConfigurationRegistryPostProcessor() {
        configBeanDefinitions = new HashSet();
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            if (isConfigurationClass(beanDefinition)){
                configBeanDefinitions.add(beanDefinition);
                new ConfigurationClassResolve(registry,beanDefinition.getBeanClass())
                        .parse();
                new ConfigurationAnnotationResolve((ConfigurableListableBeanFactory) registry,beanDefinition.getBeanClass(), configBeanDefinitions)
                        .parse();
            }
        }
    }



    protected boolean isConfigurationClass(BeanDefinition beanDefinition) {
        return beanDefinition.getBeanClass().isAnnotationPresent(Configuration.class);
    }
}
