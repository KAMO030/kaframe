package com.kamo.context.annotation;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private List<String> configNames = new ArrayList<>();
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        configNames.forEach(configName->
                new ConfigurationClassResolve(registry,
                        registry.getBeanDefinition(configName).getBeanClass())
                        .parse());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            if (isConfigurationClass(beanDefinition)){
                configNames.add(beanDefinitionName);
                new ConfigurationAnnotationResolve(beanFactory,beanDefinition.getBeanClass())
                        .parse();
            }
        }
    }

    protected boolean isConfigurationClass(BeanDefinition beanDefinition) {
        return beanDefinition.getBeanClass().isAnnotationPresent(Configuration.class);
    }
}
