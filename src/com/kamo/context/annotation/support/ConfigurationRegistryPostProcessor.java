package com.kamo.context.annotation.support;

import com.kamo.bean.BeanDefinition;
import com.kamo.bean.annotation.Configuration;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.*;
import com.kamo.core.util.AnnotationUtils;

public class ConfigurationRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, Aware {




    private ConditionMatcher classConditionMatcher;

    private ApplicationContext applicationContext;



    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            if (isConfigurationClass(beanDefinition)) {
                AnnotationConfigParser.parseConfiguration(applicationContext,classConditionMatcher,beanDefinition);
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
