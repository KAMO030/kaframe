package com.kamo.context.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public abstract class AbstractPropertyResolve implements PropertyResolve {

    public boolean needParse(Field field) {
        if (field.isAnnotationPresent(Autowired.class)) {
            return true;
        }
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.isAnnotationPresent(Autowired.class)) {
                return true;
            }
        }
        return false;
    }
}
