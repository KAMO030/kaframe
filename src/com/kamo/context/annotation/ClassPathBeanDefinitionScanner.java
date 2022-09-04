package com.kamo.context.annotation;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionBuilder;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.util.AnnotationUtils;

import java.beans.Introspector;

public class ClassPathBeanDefinitionScanner extends AbstractScanner {

    private BeanDefinitionRegistry registry;

    private ConditionMatcher conditionMatcher;

    public ClassPathBeanDefinitionScanner(ConditionMatcher conditionMatcher,BeanDefinitionRegistry registry) {
        this.conditionMatcher = conditionMatcher;
        this.registry = registry;
    }



    public void register(Class beanClass) {
        Component component = AnnotationUtils.getAnnotation(beanClass, Component.class);
        String beanName = component.value();
        if (beanName.equals("")) {
            beanName = Introspector.decapitalize(beanClass.getSimpleName());
        }
        if (registry.containsBeanDefinition(beanName)) {
            return;
        }
        BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(beanClass);
        registry.registerBeanDefinition(beanName, beanDefinition);
        if (AnnotationUtils.isAnnotationPresent(beanClass, Configuration.class)) {
            AnnotationConfigUtils.parseConfiguration((ConfigurableListableBeanFactory) registry,registry,conditionMatcher,beanClass);
        }

    }

    public boolean isRegisterClass(Class loaderClass) {

        return loaderClass != null
                && AnnotationUtils.isAnnotationPresent(loaderClass, Component.class)
                && !loaderClass.isInterface()
                && conditionMatcher.isMeeConditions(loaderClass);
    }
}



