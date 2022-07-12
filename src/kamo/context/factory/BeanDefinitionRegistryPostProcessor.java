package kamo.context.factory;

import kamo.context.BeanDefinitionRegistry;
import kamo.context.exception.BeansException;

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor{
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
