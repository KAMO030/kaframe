package com.kamo.context.annotation;

public interface AnnotationConfigRegistry {
    void register(Class<?>... componentClasses);

    void scan(String... basePackages);
}
