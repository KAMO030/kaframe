package com.kamo.transaction;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionBuilder;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.annotation.BeanDefinitionImportRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import com.kamo.transaction.support.DataSourceTransManager;
import com.kamo.util.AnnotationMetadata;

import java.beans.Introspector;

public class TransactionManagerRegistryPostProcessor extends BeanDefinitionImportRegistry implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) throws BeansException {
        Boolean annotation = (Boolean) annotationMetadata.getSrcAnnotationValue("autoRegistryManager");
        if (!annotation) {
            return;
        }
        BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(DataSourceTransManager.class);
        String name = Introspector.decapitalize(DataSourceTransManager.class.getSimpleName());
        registry.registerBeanDefinition(name,beanDefinition);
    }
}
