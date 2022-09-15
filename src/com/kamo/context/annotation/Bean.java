package com.kamo.context.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Bean {
    String name()default "";
    String[] value() default "";
}
