package com.kamo.core.support;

import com.kamo.core.exception.ReflectException;
import com.kamo.core.util.AnnotationUtils;
import com.kamo.core.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        return AnnotationUtils.isAnnotationPresent(this.getSrcAnnotationElement(),annotationType);
    }

    default <R extends Annotation> List<R>  getAnnotationsInSrc(Class<R> annotationType) {
        return AnnotationUtils.getAnnotations(this.getSrcAnnotationElement(),annotationType);
    }
    default <R extends Annotation> R getAnnotationInSrc(Class<R> annotationType) {
        return AnnotationUtils.getAnnotation(this.getSrcAnnotationElement(),annotationType);
    }
    default <T> T getAnnotationValue(String attributeName){
        return getValue(this.getAnnotation(), attributeName);
    }
    default <T > T getSrcAnnotationValue(String attributeName){
        return getValue(this.getSrcAnnotation(), attributeName);
    }
    default <T extends Object> T getValue(Annotation annotation,String attributeName){
        Objects.requireNonNull(annotation);
        Class<? extends Annotation> type = annotation.annotationType();
        try {
            Method method = ReflectUtils.getMethod(type, attributeName);
            return (T) ReflectUtils.invokeMethod(method,annotation);
        }catch (ReflectException e){
            Field field = ReflectUtils.getField(type, attributeName);
            return (T) ReflectUtils.getFieldValue(field,null);
        }
    }
}
