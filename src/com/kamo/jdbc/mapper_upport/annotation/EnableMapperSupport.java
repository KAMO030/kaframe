package com.kamo.jdbc.mapper_upport.annotation;

import com.kamo.context.annotation.Import;
import com.kamo.jdbc.mapper_upport.MapperSupportImport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({MapperSupportImport.class})
public @interface EnableMapperSupport {
    String[] value()default {};
}
