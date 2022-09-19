package com.kamo.core.cglib;

import com.kamo.core.cglib.asm.AsmProxyClassWriter;
import com.kamo.core.cglib.asm.AsmUtil;
import com.kamo.core.util.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyClass {
    private static final List<ProxyClassLoader> CLASSLOADER_CACHE = new ArrayList<>();

    public static <T> T newProxyInstance(ClassLoader classLoader, Class<T> superclass, boolean isWriteFile, InvocationHandler handler) {
        Objects.requireNonNull(handler);

        Class<T> proxyClass = getProxyClass(classLoader, superclass, isWriteFile);

        return  ReflectUtils.newInstance(proxyClass, new Class[]{InvocationHandler.class}, handler);

    }

    public static <T> T newProxyInstance(ClassLoader classLoader, Class<T> superclass, InvocationHandler handler) {
        return newProxyInstance(classLoader, superclass, false, handler);
    }

    public static <T> T newProxyInstance(Class<T> superclass, InvocationHandler handler) {
        return newProxyInstance(Thread.currentThread().getContextClassLoader(), superclass, false, handler);
    }

    public static <T> T newProxyInstance(Class<T> superclass, boolean isWriteFile, InvocationHandler handler) {
        return newProxyInstance(Thread.currentThread().getContextClassLoader(), superclass, isWriteFile, handler);
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

    private static class ProxyClassLoader extends ClassLoader {
        private final Map<String, Class> proxyClassCache = new ConcurrentHashMap<>();

        public ProxyClassLoader(ClassLoader parent) {
            super(parent);
        }

        protected Class getProxyClass(Class superclass, boolean isWriteFile) {
            String cacheKey = getProxyClassName(superclass);
            if (proxyClassCache.containsKey(cacheKey)) {
                return proxyClassCache.get(cacheKey);
            }
            ProxyClassWriter proxyClassWriter = new AsmProxyClassWriter(superclass, cacheKey);
            byte[] cacheBytes = proxyClassWriter.getProxyClassByteArray();

            if (isWriteFile) {
                int index = cacheKey.lastIndexOf('.');
                if (index == -1) {
                    throw new IllegalArgumentException("");
                }
                String superClassFile = cacheKey.substring(0, index).replace(".", "/");

                String proxyClassFile = cacheKey.substring(index + 1) + ".class";
                String filePath = this.getResource(superClassFile).getFile();
                AsmUtil.writeBytes(filePath + "/" + proxyClassFile, cacheBytes);
            }

            Class<?> proxyClass = defineClass(cacheKey, cacheBytes, 0, cacheBytes.length);
            proxyClassCache.put(cacheKey, proxyClass);
            return proxyClass;
        }

    }

    public static String getProxyClassName(Class superclass) {
        String superclassName = superclass.getName();

        return superclassName + "$Proxy$" + Math.abs(superclassName.hashCode());
    }

    public static Class getProxyClass(ClassLoader classLoader, Class superclass, boolean isWriteFile) {
        Objects.requireNonNull(classLoader);
        Objects.requireNonNull(superclass);
        if (superclass.isInterface()) {
            throw new IllegalArgumentException(superclass + " 为接口类型,无法创建代理");
        }
        ProxyClassLoader matchLoader = matchClassLoader(classLoader);

        return matchLoader.getProxyClass(superclass, isWriteFile);
    }


}
