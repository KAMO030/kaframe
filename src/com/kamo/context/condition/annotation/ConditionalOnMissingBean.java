package com.kamo.context.condition.annotation;

import com.kamo.context.condition.impl.ConditionOnBean;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ConditionOnBean.class)
public @interface ConditionalOnMissingBean {
    boolean IS_MISS = true;
    String[] beanNames() default {};
    String[] beanTypes() default {};
    Class[] beanClasses() default {};
}
