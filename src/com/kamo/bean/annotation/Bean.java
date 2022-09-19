package com.kamo.bean.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Bean {
    String name()default "";
    String[] value() default "";
}
