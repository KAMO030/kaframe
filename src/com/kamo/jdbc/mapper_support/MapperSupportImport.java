package com.kamo.jdbc.mapper_support;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionBuilder;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;

public class MapperSupportImport implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinition factory = BeanDefinitionBuilder.getBeanDefinition(MapperSupportFactory.class);
        BeanDefinition factoryBean = BeanDefinitionBuilder.getBeanDefinition(MapperSupportFactoryBean.class);
        registry.registerBeanDefinition(MapperSupportFactory.class.getSimpleName(),factory);
        registry.registerBeanDefinition(MapperSupportFactoryBean.class.getSimpleName(),factoryBean);
    }
}
