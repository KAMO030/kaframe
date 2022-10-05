package com.kamo.core.cglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface MethodProxy {
    static Object invoke(Object instance, Method method,Object[]args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }
    static Object invokeSuper(Object instance, Method method,Object[]args) throws InvocationTargetException, IllegalAccessException {
        try {
            Method superMethod = instance.getClass().getMethod(method.getName()+"$Proxy",method.getParameterTypes());
            return superMethod.invoke(instance, args);
        }  catch (NoSuchMethodException e) {
            return method.invoke(instance, args);
        }
    }
}
