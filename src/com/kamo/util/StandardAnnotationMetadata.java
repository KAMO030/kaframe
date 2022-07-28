package com.kamo.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class StandardAnnotationMetadata<T extends Annotation> implements AnnotationMetadata<T> {
    private Annotation srcAnnotation;
    private T annotation;
    private Class<T> annotationType;
    private AnnotatedElement srcAnnotationElement;

    public StandardAnnotationMetadata(T annotation, Class<T> annotationType, AnnotatedElement srcAnnotationElement) {
        this.annotation = annotation;
        this.annotationType = annotationType;
        this.srcAnnotationElement = srcAnnotationElement;
    }

    public void setSrcAnnotation(Annotation srcAnnotation) {
        this.srcAnnotation = srcAnnotation;
    }

    public T getAnnotation() {
        return annotation;
    }

    @Override
    public Annotation getSrcAnnotation() {
        return srcAnnotation;
    }

    @Override
    public Class getAnnotationType() {
        return annotationType;
    }

    @Override
    public AnnotatedElement getSrcAnnotationElement() {
        return srcAnnotationElement;
    }

    @Override
    public String toString() {
        return "StandardAnnotationMetadata{" +
                "annotation=" + annotation +
                ", annotationType=" + annotationType +
                ", srcAnnotationElement=" + srcAnnotationElement +
                '}';
    }
}
