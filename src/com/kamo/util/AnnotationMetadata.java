package com.kamo.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public  interface   AnnotationMetadata<T extends Annotation> {
    T getAnnotation();
    Annotation getSrcAnnotation();
    Class<T> getAnnotationType();
    AnnotatedElement getSrcAnnotationElement();
    default String getSrcAnnotationElementClassName(){
        return ((Class) getSrcAnnotationElement()).getName();
    };
    default Class getSrcAnnotationElementClass(){
        return ((Class) getSrcAnnotationElement());
    };
    default boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return ReflectUtils.isAnnotationPresent(this.getSrcAnnotationElement(),annotationType);
    }

    default <R extends Annotation> List<R>  getAnnotationsInSrc(Class<R> annotationType) {
        return ReflectUtils.getAnnotations(this.getSrcAnnotationElement(),annotationType);
    }
    default <R extends Annotation> R getAnnotationInSrc(Class<R> annotationType) {
        return ReflectUtils.getAnnotation(this.getSrcAnnotationElement(),annotationType);
    }
    default Object getSrcAnnotationValue(String attributeName){
        Annotation annotation = this.getSrcAnnotation();
        Objects.requireNonNull(annotation);
        try {
            return annotation.annotationType().getDeclaredMethod(attributeName).invoke(annotation);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    };
}
