package com.kamo.core.util;

public class ClassUtils {
    public static  ClassLoader getDefaultClassLoader(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ReflectUtils.class.getClassLoader();
        }
        return classLoader;
    }
}
