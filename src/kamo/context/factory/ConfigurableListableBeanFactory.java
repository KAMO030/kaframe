package kamo.context.factory;

import kamo.context.BeanDefinition;
import kamo.context.exception.NoSuchBeanDefinitionException;

public interface ConfigurableListableBeanFactory {
    void registerConfiguration(Object regist);
    String[] getBeanDefinitionNames();
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
}
