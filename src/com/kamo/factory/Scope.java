package com.kamo.factory;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Deprecated

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Scope {
    String value() default SINGLETON;
    String SINGLETON="singleton";
    String PROTOTYPE="prototype";
}
