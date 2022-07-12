package kamo.context.annotation;

import kamo.context.BeanDefinition;
import kamo.context.BeanDefinitionRegistry;
import kamo.context.exception.BeansException;
import kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import kamo.context.factory.ConfigurableListableBeanFactory;

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
