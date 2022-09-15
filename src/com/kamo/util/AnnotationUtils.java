package com.kamo.util;






import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.function.Consumer;

public final class AnnotationUtils {
    private AnnotationUtils() {
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
            if (AnnotationUtils.isAnnotationPresent(type, annotationType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得该元素上的的传入注解,如果不存在返回null
     *
     * @param element        元素
     * @param annotationType 需要的注解类型
     * @param <T>            类型需继承Annotation类
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
            if (AnnotationUtils.isAnnotationPresent(type, annotationType)) {
                return AnnotationUtils.getAnnotation(type, element, annotationType);
            }
        }
        return null;
    }

    /**
     * 递归过滤条件
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
            if (AnnotationUtils.isAnnotationPresent(type, annotationType)) {
                AnnotationUtils.getAnnotations(type, element, annotationType, list);
            }
        }
        return list;
    }

    private static <R extends Annotation> R doGetAnnotation(AnnotatedElement element, AnnotatedElement oldElement,
                                                            Class<R> annotationType) {
        return element.equals(oldElement) ?
                element.getAnnotation(annotationType) :
                (R) Proxy.newProxyInstance(AnnotationUtils.class.getClassLoader(), new Class[]{annotationType},
                        new AnnotationConvertHandler(element, oldElement, annotationType));
    }

    public static <R extends Annotation> AnnotationMetadata<R> getAnnotationMetadata(AnnotatedElement element, Class<R> annotationType) {
        return getAnnotationMetadata(element, element, annotationType);
    }


    protected static <R extends Annotation> AnnotationMetadata<R> getAnnotationMetadata(AnnotatedElement element, AnnotatedElement oldElement, Class<R> annotationType) {
        if (element.isAnnotationPresent(annotationType)) {
            R meta = doGetAnnotation(element, oldElement, annotationType);
            StandardAnnotationMetadata<R> standardAnnotationMetadata = new StandardAnnotationMetadata<>(meta, annotationType, oldElement);
            if (!element.equals(oldElement)) {
                Class srcAnnotationType = ((Class) element).asSubclass(Annotation.class);
                standardAnnotationMetadata.setSrcAnnotation(AnnotationUtils.getAnnotation(oldElement, srcAnnotationType));
            }
            return standardAnnotationMetadata;
        }
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (annotationFilter(type)) {
                continue;
            }
            if (AnnotationUtils.isAnnotationPresent(type, annotationType)) {
                return AnnotationUtils.getAnnotationMetadata(type, element, annotationType);
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
                standardAnnotationMetadata.setSrcAnnotation(AnnotationUtils.getAnnotation(oldElement, srcAnnotationType));
            }
            list.add(standardAnnotationMetadata);
        }
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (annotationFilter(type)) {
                continue;
            }
            if (AnnotationUtils.isAnnotationPresent(type, annotationType)) {
                AnnotationUtils.getAnnotationMetadatas(type, element, annotationType, list);
            }
        }
        return list;
    }

    public static <T extends Annotation> void getAnnotationAndHandle(Class<T> type, Method[] methods, Consumer<T> handler) {
        for (Method method : methods) {
            T annotation = AnnotationUtils.getAnnotation(method, type);
            if (annotation == null) {
                continue;
            }
            handler.accept(annotation);
        }
    }

    public static <T> T getValue(AnnotatedElement element, Class<? extends Annotation> annotationType, T defaultValue) {
        return getValue(element,annotationType, "value", defaultValue);
    }

    public static <T> T getValue(AnnotatedElement element, Class<? extends Annotation> annotationType, String methodName, T defaultValue) {
        if (!element.isAnnotationPresent(annotationType)) {
            return defaultValue;
        }
        Annotation annotation = element.getAnnotation(annotationType);
        try {
            T value = (T)annotationType.getMethod(methodName).invoke(annotation,new Object[0]);
            if (value instanceof String && value.equals("")) {
                return defaultValue;
            }
            return value;
        }catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
