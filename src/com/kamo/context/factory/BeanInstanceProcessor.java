package com.kamo.context.factory;

import com.kamo.context.BeanDefinition;

public interface BeanInstanceProcessor extends ApplicationProcessor {



    default Object instanceBefore(String beanName, BeanDefinition beanDefinition){return null;};

   default void instanceAfter(String beanName, BeanDefinition beanDefinition, Object bean){};
}
