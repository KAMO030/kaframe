package com.kamo.core.cglib;

import com.kamo.core.util.ClassUtils;
import com.kamo.core.util.ObjectUtils;
import com.kamo.core.util.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.List;

public class ProxyClass {
    private static final List<ProxyClassLoader> CLASSLOADER_CACHE = new ArrayList<>();

    public static <T> T newProxyInstance(ClassLoader classLoader, Class<T> superclass, InvocationHandler handler) {
        return newProxyInstance(classLoader, superclass, false, handler);
    }

    public static <T> T newProxyInstance(Class<T> superclass, InvocationHandler handler) {
        return newProxyInstance(null, superclass, false, handler);
    }

    public static <T> T newProxyInstance(Class<T> superclass, boolean isWriteFile, InvocationHandler handler) {
        return newProxyInstance(null, superclass, isWriteFile, handler);
    }

    public static <T> T newProxyInstance(ClassLoader classLoader, Class<T> superclass, boolean isWriteFile, InvocationHandler handler) {
        return newProxyInstance(classLoader, superclass, isWriteFile, handler,null,null);
    }

    public static <T> T newProxyInstance(ClassLoader classLoader, Class<T> superclass, boolean isWriteFile, InvocationHandler handler, Class[] types, Object... args) {
        ObjectUtils.requireNonNull(handler, superclass);

        Class<T> proxyClass = getProxyClass(classLoader, superclass, isWriteFile);

        return doNewProxyInstance(proxyClass, handler, types, args);
    }

    private static <T> T doNewProxyInstance(Class<T> proxyClass, InvocationHandler handler, Class[] types, Object[] args) {
        //(args != null && args.length != 0) && (argTypes == null || argTypes.length != args.length)
        if (!ReflectUtils.needAutoParserTypes(types, args)) {
            return ReflectUtils.newInstance(proxyClass, new Class[]{InvocationHandler.class}, handler);
        }

        Object[] newArgs = new Object[args.length + 1];
        newArgs[newArgs.length - 1] = handler;

        Class[] newTypes = new Class[types.length + 1];
        newTypes[newTypes.length - 1] = InvocationHandler.class;

        return ReflectUtils.newInstance(proxyClass, newTypes, newArgs);
    }

    public static Class getProxyClass(ClassLoader classLoader, Class superclass, boolean isWriteFile) {
        ObjectUtils.requireNonNull(superclass);

        if (classLoader == null) {
            classLoader = ClassUtils.getDefaultClassLoader();
        }

        if (superclass.isInterface()) {
            throw new IllegalArgumentException(superclass + " 为接口类型,无法创建代理");
        }
        ProxyClassLoader matchLoader = matchClassLoader(classLoader);

        return matchLoader.getProxyClass(superclass, isWriteFile);
    }

    private static ProxyClassLoader matchClassLoader(ClassLoader classLoader) {
        ProxyClassLoader matchLoader = null;
        for (ProxyClassLoader loader : CLASSLOADER_CACHE) {
            if (loader.getParent().equals(classLoader)) {
                matchLoader = loader;
                break;
            }
        }
        if (matchLoader == null) {
            matchLoader = new ProxyClassLoader(classLoader);
            CLASSLOADER_CACHE.add(matchLoader);
        }
        return matchLoader;
    }


    public static String getProxyClassName(Class superclass) {
        String superclassName = superclass.getName();

        return superclassName + "$Proxy$" + Math.abs(superclassName.hashCode());
    }


}
