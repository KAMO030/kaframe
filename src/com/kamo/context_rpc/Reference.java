package com.kamo.context_rpc;

import com.kamo.context.annotation.Autowired;
import com.kamo.util.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Autowired
public @interface Reference {
    @AliasFor(annotation = Autowired.class)
    String value();
}
