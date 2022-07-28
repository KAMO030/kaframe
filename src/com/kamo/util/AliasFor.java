package com.kamo.util;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AliasFor {

    String value() default "";

    String attribute() default "";

    Class<? extends Annotation> annotation();
}
