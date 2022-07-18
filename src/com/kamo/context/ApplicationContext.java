package com.kamo.context;

import com.kamo.context.factory.ConfigurableListableBeanFactory;


public interface ApplicationContext extends BeanDefinitionRegistry,BeanFactory, ConfigurableListableBeanFactory {
}
