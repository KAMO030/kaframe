package com.kamo.transaction;

import com.kamo.bean.support.AnnotationBeanDefinitionBuilder;
import com.kamo.context.annotation.support.BeanDefinitionImportRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.BeanDefinitionRegistry;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import com.kamo.core.support.AnnotationMetadata;
import com.kamo.transaction.support.DataSourceTransManager;

import java.beans.Introspector;

public class TransactionManagerRegistryPostProcessor extends BeanDefinitionImportRegistry implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) throws BeansException {
        Boolean annotation = (Boolean) annotationMetadata.getSrcAnnotationValue("autoRegistryManager");
        if (!annotation) {
            return;
        }
        BeanDefinition beanDefinition = AnnotationBeanDefinitionBuilder.getBeanDefinition(DataSourceTransManager.class);
        String name = Introspector.decapitalize(DataSourceTransManager.class.getSimpleName());
        registry.registerBeanDefinition(name,beanDefinition);
    }
}
