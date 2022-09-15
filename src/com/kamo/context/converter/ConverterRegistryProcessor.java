package com.kamo.context.converter;

import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanPostProcessor;

public class ConverterRegistryProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Converter) {
            ConverterRegistry.registerConverter((Converter) bean);
        }
        return bean;
    }

}
