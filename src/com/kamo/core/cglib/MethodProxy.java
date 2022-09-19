package com.kamo.core.cglib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface MethodProxy {
    static Object invoke(Object instance, Method method,Object[]args){
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    static Object invokeSuper(Object instance, Method method,Object[]args){
        try {
            Method superMethod = instance.getClass().getMethod(method.getName()+"$Proxy",method.getParameterTypes());
            return superMethod.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
