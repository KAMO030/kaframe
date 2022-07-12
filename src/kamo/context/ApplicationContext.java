package kamo.context;

import kamo.context.factory.ConfigurableListableBeanFactory;


public interface ApplicationContext extends BeanDefinitionRegistry,BeanFactory, ConfigurableListableBeanFactory {
}
