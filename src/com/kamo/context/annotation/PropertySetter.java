package com.kamo.context.annotation;

import com.kamo.bean.BeanDefinition;

public interface PropertySetter {
    void setBeanProperty(BeanDefinition beanDefinition, Object bean);
}
