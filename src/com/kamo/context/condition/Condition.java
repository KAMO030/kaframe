package com.kamo.context.condition;

import com.kamo.util.AnnotationMetadata;

import java.lang.reflect.AnnotatedElement;

@FunctionalInterface
public interface Condition {
    boolean matches(AnnotationMetadata metadata, AnnotatedElement element);
}
