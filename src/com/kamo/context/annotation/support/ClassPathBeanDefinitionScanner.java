package com.kamo.context.annotation.support;

import com.kamo.bean.annotation.Component;
import com.kamo.bean.annotation.Configuration;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.condition.impl.AnnotationConditionMatcher;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.bean.BeanDefinition;
import com.kamo.core.util.AnnotationUtils;

import java.beans.Introspector;

public class ClassPathBeanDefinitionScanner extends AbstractScanner {

    private ApplicationContext context;

    private ConditionMatcher conditionMatcher;

    public ClassPathBeanDefinitionScanner(ConditionMatcher conditionMatcher,ApplicationContext context) {
        this.conditionMatcher = conditionMatcher;
        this.context = context;
    }



    public void register(Class beanClass) {
        Component component = AnnotationUtils.getAnnotation(beanClass, Component.class);
        String beanName = component.value();
        if (beanName.equals("")) {
            beanName = Introspector.decapitalize(beanClass.getSimpleName());
        }
        if (context.containsBeanDefinition(beanName)) {
            return;
        }
        BeanDefinition beanDefinition = context.registerBeanDefinition(beanName, beanClass);
        if (AnnotationUtils.isAnnotationPresent(beanClass, Configuration.class)) {
            AnnotationConfigParser.parseConfiguration( context,conditionMatcher,beanDefinition);
        }

    }

    public boolean isRegisterClass(Class loaderClass) {

        return loaderClass != null
                && AnnotationUtils.isAnnotationPresent(loaderClass, Component.class)
                && !loaderClass.isInterface()
                && conditionMatcher.isMeeConditions(loaderClass);
    }
}



