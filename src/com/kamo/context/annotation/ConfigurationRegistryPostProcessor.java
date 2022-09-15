package com.kamo.context.annotation;

import com.kamo.context.ApplicationContext;
import com.kamo.context.Aware;
import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.ApplicationContextAware;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import com.kamo.context.condition.ConditionMatcherAware;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.util.AnnotationUtils;

import java.util.HashSet;
import java.util.Set;

public class ConfigurationRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, Aware {




    private ConditionMatcher classConditionMatcher;

    private ApplicationContext applicationContext;



    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            if (isConfigurationClass(beanDefinition)) {
                AnnotationConfigUtils.parseConfiguration(applicationContext,classConditionMatcher,beanDefinition);
            }
        }
    }


    protected boolean isConfigurationClass(BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();
        return classConditionMatcher.isMeeConditions(beanClass)
                && AnnotationUtils.isAnnotationPresent(beanClass, Configuration.class);
    }


    @Override
    public void setAware(Object... awareValue) {
        classConditionMatcher = (ConditionMatcher) awareValue[0];
        applicationContext = (ApplicationContext) awareValue[1];
    }

    @Override
    public Class[] getAwareTypes() {
        return new Class[] {
                ConditionMatcher.class,
                ApplicationContext.class
        };
    }


}
