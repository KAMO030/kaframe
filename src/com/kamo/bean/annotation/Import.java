package com.kamo.bean.annotation;

import com.kamo.context.factory.ApplicationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Import {
    Class<? extends ApplicationProcessor>[]value();
}
