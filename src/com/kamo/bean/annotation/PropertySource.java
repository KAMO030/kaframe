package com.kamo.bean.annotation;

import com.kamo.context.annotation.support.PropertySourceConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(PropertySourceConfig.class)
public @interface PropertySource {
    String[] value();
}
