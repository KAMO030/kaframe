package com.kamo.context.condition.annotation;

import com.kamo.context.condition.Condition;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
    Class<? extends Condition>[] value();
}
