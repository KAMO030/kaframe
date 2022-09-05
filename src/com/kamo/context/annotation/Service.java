package com.kamo.context.annotation;


import com.kamo.util.AliasFor;

import java.lang.annotation.*;
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Component
public @interface Service {
    @AliasFor(annotation = Component.class)
    String value() default "";
}


