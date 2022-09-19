package com.kamo.context.condition;

import com.kamo.core.support.AnnotationMetadata;

import java.lang.reflect.AnnotatedElement;

@FunctionalInterface
public interface Condition {
    boolean matches(AnnotationMetadata metadata, AnnotatedElement element);
}
