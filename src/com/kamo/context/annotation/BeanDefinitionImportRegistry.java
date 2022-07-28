package com.kamo.context.annotation;

import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.util.AnnotationMetadata;

public abstract class BeanDefinitionImportRegistry implements BeanDefinitionRegistryPostProcessor {
    @Autowired
    private AnnotationMetadata annotationMetadata;
    @Override
    public final  void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        postProcessBeanFactory(annotationMetadata,beanFactory);
    }

    @Override
    public final void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        postProcessBeanDefinitionRegistry(annotationMetadata,registry);
    }
    public void postProcessBeanFactory(AnnotationMetadata annotationMetadata,ConfigurableListableBeanFactory beanFactory) {

    }

    public void postProcessBeanDefinitionRegistry(AnnotationMetadata annotationMetadata,BeanDefinitionRegistry registry) throws BeansException {

    }

}
