package kamo.context.factory;

import kamo.context.BeanDefinition;

public interface BeanInstanceProcessor {



    default Object instanceBefore(String beanName, BeanDefinition beanDefinition){return null;};

   default void instanceAfter(String beanName, BeanDefinition beanDefinition, Object bean){};
}
