package com.kamo.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public final class ReflectUtils {
    private ReflectUtils() {

    }
    public static <T> Class getWrapperClass(Class<T> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        String typeName = type.getName();
        if (typeName.equals("byte"))
            return Byte.class;
        if (typeName.equals("short"))
            return Short.class;
        if (typeName.equals("int"))
            return Integer.class;
        if (typeName.equals("long"))
            return Long.class;
        if (typeName.equals("char"))
            return Character.class;
        if (typeName.equals("float"))
            return Float.class;
        if (typeName.equals("double"))
            return Double.class;
        if (typeName.equals("boolean"))
            return Boolean.class;
        if (typeName.equals("void"))
            return Void.class;
        throw new IllegalArgumentException("Not primitive type :"+typeName);
    }
    public static boolean isPrimitive(Class type) {
        if (type.isPrimitive() || String.class.equals(type)) {
            return true;
        }
        try {
            Field field = type.getDeclaredField("TYPE");
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static boolean isAnnotationPresent(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        if (element.isAnnotationPresent(annotationType)) {
            return true;
        }
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (annotationFilter(type)) {
                continue;
            }
            if (ReflectUtils.isAnnotationPresent(type, annotationType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得该元素上的的传入注解,如果不存在返回null
     * @param element 元素
     * @param annotationType 需要的注解类型
     * @param <T> 类型需继承Annotation类
     * @return 需要的注解实例
     */
    public static <T extends Annotation> T getAnnotation(AnnotatedElement element, Class<T> annotationType) {
        return getAnnotation(element, element, annotationType);
    }

    protected static <R extends Annotation> R getAnnotation(AnnotatedElement element, AnnotatedElement oldElement, Class<R> annotationType) {
        if (element.isAnnotationPresent(annotationType)) {
            return doGetAnnotation(element, oldElement, annotationType);
        }
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (annotationFilter(type)) {
                continue;
            }
            if (ReflectUtils.isAnnotationPresent(type, annotationType)) {
                return ReflectUtils.getAnnotation(type, element, annotationType);
            }
        }
        return null;
    }
    /**
    递归过滤条件
     */
    public static boolean annotationFilter(Class<? extends Annotation> type) {
        return type.equals(Documented.class) || type.equals(Target.class) || type.equals(Retention.class);
    }

    public static <R extends Annotation> List<R> getAnnotations(AnnotatedElement element, Class<R> annotationType) {
        return getAnnotations(element, element, annotationType, new ArrayList<>());
    }


    protected static <R extends Annotation> List<R> getAnnotations(AnnotatedElement element, AnnotatedElement oldElement,
                                                                   Class<R> annotationType, List<R> list) {
        if (element.isAnnotationPresent(annotationType)) {
            list.add(doGetAnnotation(element, oldElement, annotationType));
        }
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (annotationFilter(type)) {
                continue;
            }
            if (ReflectUtils.isAnnotationPresent(type, annotationType)) {
                ReflectUtils.getAnnotations(type, element, annotationType, list);
            }
        }
        return list;
    }

    private static <R extends Annotation> R doGetAnnotation(AnnotatedElement element, AnnotatedElement oldElement,
                                                            Class<R> annotationType) {
        return element.equals(oldElement) ?
                element.getAnnotation(annotationType) :
                (R) Proxy.newProxyInstance(ReflectUtils.class.getClassLoader(), new Class[]{annotationType},
                        new AnnotationConvertHandler(element, oldElement, annotationType));
    }

    public static <R extends Annotation> AnnotationMetadata<R> getAnnotationMetadata(AnnotatedElement element, Class<R> annotationType) {
        return getAnnotationMetadata(element, element, annotationType);
    }


    protected static <R extends Annotation> AnnotationMetadata<R> getAnnotationMetadata(AnnotatedElement element, AnnotatedElement oldElement, Class<R> annotationType) {
        if (element.isAnnotationPresent(annotationType)) {
            R meta = doGetAnnotation(element, oldElement, annotationType);
            StandardAnnotationMetadata<R> standardAnnotationMetadata = new StandardAnnotationMetadata<>(meta, annotationType, oldElement);
            if (element instanceof Annotation){
                standardAnnotationMetadata.setSrcAnnotation((Annotation) element);
            }
            return standardAnnotationMetadata;
        }
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (annotationFilter(type)) {
                continue;
            }
            if (ReflectUtils.isAnnotationPresent(type, annotationType)) {
                return ReflectUtils.getAnnotationMetadata(type, element, annotationType);
            }
        }
        return null;
    }

    public static <R extends Annotation> List<AnnotationMetadata<R>> getAnnotationMetadatas(AnnotatedElement element, Class<R> annotationType) {
        return getAnnotationMetadatas(element, element, annotationType, new ArrayList<>());
    }

    protected static <R extends Annotation> List<AnnotationMetadata<R>> getAnnotationMetadatas(AnnotatedElement element, AnnotatedElement oldElement,
                                                                                               Class<R> annotationType, List<AnnotationMetadata<R>> list) {
        if (element.isAnnotationPresent(annotationType)) {
            R meta = doGetAnnotation(element, oldElement, annotationType);
            StandardAnnotationMetadata<R> standardAnnotationMetadata = new StandardAnnotationMetadata<>(meta, annotationType, oldElement);
            if (!element.equals(oldElement)) {
                    Class srcAnnotationType = ((Class) element).asSubclass(Annotation.class);
                standardAnnotationMetadata.setSrcAnnotation(ReflectUtils.getAnnotation(oldElement, srcAnnotationType));
            }
            list.add(standardAnnotationMetadata);
        }
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (annotationFilter(type)) {
                continue;
            }
            if (ReflectUtils.isAnnotationPresent(type, annotationType)) {
                ReflectUtils.getAnnotationMetadatas(type, element, annotationType, list);
            }
        }
        return list;
    }
}
