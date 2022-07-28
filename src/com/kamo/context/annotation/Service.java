package com.kamo.context.annotation;


import com.kamo.util.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Component
public @interface Service {
    @AliasFor(annotation = Component.class)
    String value() default "";
}


