package com.kamo.context;

import com.kamo.core.util.ClassUtils;
import com.kamo.core.util.ListUtils;
import com.kamo.core.util.ReflectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class FactoryLoader {
    private static Map<String, List<String>> FACTORY_CACHE;
    private static final String FACTORY_PATH = "META-INF/kamo.factories";

    private FactoryLoader() {
    }

    public static Class[] load(Class factoryClass) {
        return load(factoryClass, ClassUtils.getDefaultClassLoader());
    }

    public static Class[] load(Class factoryClass, ClassLoader classLoader) {
        return load(factoryClass.getName(), classLoader);
    }

    public static Class[] load(String factoryClassName) {
        return load(factoryClassName,  ClassUtils.getDefaultClassLoader());
    }

    public static Class[] load(String factoryClassName, ClassLoader classLoader) {

        if (FACTORY_CACHE == null) {
            try {
                loadCache(classLoader);
            } catch (IOException e) {
                e.printStackTrace();
                return new Class[0];
            }
        }
        if (!FACTORY_CACHE.containsKey(factoryClassName)) {
            return new Class[0];
        }
        List<String> classNames = FACTORY_CACHE.get(factoryClassName);
        List<Class> classList = new ArrayList<>();
        for (String name : classNames) {
            classList.add(ReflectUtils.loadClass(classLoader, name));
        }
        return classList.toArray(new Class[0]);
    }

    private static void loadCache(ClassLoader contextClassLoader) throws IOException {
        FACTORY_CACHE = new ConcurrentHashMap<>();
        Enumeration<URL> resources = contextClassLoader.getResources(FACTORY_PATH);
        while (resources.hasMoreElements()) {
            doLoad(resources.nextElement());
        }
        //去重
        for (String interfacesName : FACTORY_CACHE.keySet()) {
            List<String> oldList = FACTORY_CACHE.get(interfacesName);
            List<String> newList = ListUtils.deduplication(oldList);
            FACTORY_CACHE.replace(interfacesName, oldList, newList);
        }
    }

    private static void doLoad(URL factoryURL) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = factoryURL.openStream()) {
            properties.load(inputStream);
            Set<String> interfacesNames = properties.stringPropertyNames();
            for (String interfacesName : interfacesNames) {
                String[] factoryNameArray = properties.getProperty(interfacesName).split(",");
                List<String> factoryNames = ReflectUtils.array2List(factoryNameArray);
                if (FACTORY_CACHE.containsKey(interfacesName)) {
                    FACTORY_CACHE.get(interfacesName).addAll(factoryNames);
                } else {
                    FACTORY_CACHE.put(interfacesName, factoryNames);
                }
            }
        }
    }
}
