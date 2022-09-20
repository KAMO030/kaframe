package com.kamo.context.condition.impl;

import com.kamo.bean.support.AnnotationBeanDefinitionBuilder;
import com.kamo.context.condition.Condition;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.condition.annotation.Conditional;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.bean.BeanDefinition;
import com.kamo.core.support.AnnotationMetadata;
import com.kamo.core.util.AnnotationUtils;

import java.lang.reflect.AnnotatedElement;

public class AnnotationConditionMatcher implements ConditionMatcher {
    private ApplicationContext applicationContext;

    public AnnotationConditionMatcher(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public boolean isMeeConditions(AnnotatedElement element) {
        if (!AnnotationUtils.isAnnotationPresent(element,Conditional.class)){
            return true;
        }
        AnnotationMetadata<Conditional> annotationMetadata = AnnotationUtils.getAnnotationMetadata(element,Conditional.class);
        Class<? extends Condition>[] value = annotationMetadata.getAnnotation().value();
        for (Class<? extends Condition> conditionClass : value) {
            Condition condition = getConditionBean(conditionClass);
            if (!condition.matches(annotationMetadata, element)) {
                return false;
            }
        }
        return true;
    }

    private Condition getConditionBean(Class<? extends Condition> conditionClass) {
        String conditionName = conditionClass.getName();
        if (!applicationContext.containsBeanDefinition(conditionName)) {
            BeanDefinition beanDefinition = AnnotationBeanDefinitionBuilder.getBeanDefinition(conditionClass);
            applicationContext.registerBeanDefinition(conditionName,beanDefinition);
        }
        return applicationContext.getBean(conditionName);
    }

    @Override
    public boolean isMeeConditions(Object obj) {
        if (obj instanceof AnnotatedElement) {
            AnnotatedElement element = (AnnotatedElement) obj;
            return isMeeConditions(element);
        }
        throw new IllegalArgumentException("["+obj+"] 参数不是条件匹配器支持类型");
    }


}
