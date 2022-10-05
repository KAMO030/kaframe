package com.kamo.context.converter;

import com.kamo.core.annotation.Order;
import com.kamo.context.factory.BeanFactoryPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
@Order(1)
public class ConverterRegistryProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }



}
