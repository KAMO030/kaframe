package com.kamo.context.factory;

import com.kamo.bean.BeanDefinition;

import java.util.List;
import java.util.Set;

public interface ConfigurableListableBeanFactory {
    void registerProcessor(String beanName , BeanDefinition beanDefinition);
    BeanDefinition registerProcessor(Class configClass);




//    List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors();
//
//    Set<BeanInstanceProcessor> getBeanInstanceProcessors();
//
//    Set<BeanPostProcessor> getBeanPostProcessors();


}
