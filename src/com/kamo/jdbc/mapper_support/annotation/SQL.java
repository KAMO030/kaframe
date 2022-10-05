package com.kamo.jdbc.mapper_support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SQL {
    String value() default "";
    String dynamicSqlStaticMethodName() default "";
    Class dynamicSqlMethodClass() default SQL.class;
}
