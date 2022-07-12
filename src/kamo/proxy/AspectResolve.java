package kamo.proxy;

import kamo.context.Resolve;
import kamo.proxy.annotation.After;
import kamo.proxy.annotation.Before;
import kamo.proxy.impl.AfterPoint;
import kamo.proxy.impl.BeforePoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class AspectResolve implements Resolve {
    private final Class aspectClass;
    private final static Map<Class, A> POINT_MAP = new HashMap<>();
    static {
        POINT_MAP.put(After.class, new AfterPoint());
        POINT_MAP.put(Before.class, new BeforePoint());
    }
    public AspectResolve(Class aspectClass) {
        this.aspectClass = aspectClass;
    }

    @Override
    public void parse() {
        Object o = null;
        try {
            o = aspectClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Method[] methods = aspectClass.getDeclaredMethods();
        for (Method method : methods) {
            A point = getPoint(method);
            if (point != null) {
                AdvisorRegister.registerAdvisor(point.getAdvisor(method,o));
            }
        }
    }

    private A getPoint(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            Class annotationType = annotation.annotationType();
            if(POINT_MAP.containsKey(annotationType)){
                return POINT_MAP.get(annotationType);
            }
        }
        return null;
    }

    private boolean isPoint(Method method) {
        return POINT_MAP.containsKey(method);

    }

}
