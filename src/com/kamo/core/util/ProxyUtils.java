package com.kamo.core.util;

import com.kamo.core.cglib.ProxyClass;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public final class ProxyUtils {
    private ProxyUtils() {
    }
    public static Object creatProxyInstance(Class targetClass, InvocationHandler handler){
        return creatProxyInstance(targetClass.getClassLoader(),targetClass,handler);
    }
    public static Object creatProxyInstance(ClassLoader classLoader, Class targetClass, InvocationHandler handler) {
        if (targetClass.isInterface()) {
            return Proxy.newProxyInstance(classLoader, new Class[]{targetClass}, handler);
        }
        Class<?>[] interfaces = targetClass.getInterfaces();

        return interfaces.length > 0 ?
                Proxy.newProxyInstance(classLoader, interfaces, handler) :
                ProxyClass.newProxyInstance(classLoader, targetClass, handler);
    }
}
