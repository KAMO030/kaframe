package com.kamo.context.annotation;

import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.factory.ConfigurableListableBeanFactory;

public final class AnnotationConfigUtils {
    public static void parseConfiguration(ConfigurableListableBeanFactory configRegistry,BeanDefinitionRegistry beanDefinitionRegistry, ConditionMatcher conditionMatcher, Class configClass) {
        new ConfigurationClassResolve(beanDefinitionRegistry, conditionMatcher, configClass)
                .parse();
        new ConfigurationAnnotationResolve( configRegistry, conditionMatcher,configClass)
                .parse();
    }

}
