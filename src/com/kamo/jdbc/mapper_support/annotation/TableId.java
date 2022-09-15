package com.kamo.jdbc.mapper_support.annotation;

import com.kamo.util.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@TableField
public @interface TableId {
    @AliasFor(annotation = TableField.class)
    String value() default "";
    IdType type ()default IdType.ASSIGN_UUID;
}
