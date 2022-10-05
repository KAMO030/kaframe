package com.kamo.context;

import com.kamo.core.util.ClassUtils;
import com.kamo.core.util.ListUtils;
import com.kamo.core.util.ReflectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class FactoryLoader<T> {
    private  List<String> classNameCache;
    private final Class<T> factoryInterfaces;
    private final String factoryInterfacesName;
    private final ClassLoader classLoader;
    private List<Class<? extends T>> classCache;
    private List<T> instanceCache;

    private static Map<String, FactoryLoader> FACTORY_CACHE;

    private static final String FACTORY_PATH = "META-INF/kamo.factories";

    private FactoryLoader(List<String> classNameCache, String factoryInterfacesName, ClassLoader classLoader) {
        this.classNameCache = classNameCache;
        this.factoryInterfacesName = factoryInterfacesName;
        this.classLoader = classLoader;
        this.factoryInterfaces = ClassUtils.loadClass(classLoader,factoryInterfacesName);
    }

    public List<? extends T> getInstance() {
        if (instanceCache == null) {
            createInstance();
        }
        return new ArrayList<>(instanceCache);
    }

    public Class<T> getFactoryInterfaces() {
        return factoryInterfaces;
    }

    public String getFactoryInterfacesName() {
        return factoryInterfacesName;
    }

    private void createInstance() {
        instanceCache = new ArrayList<>();
        Class<? extends T>[] factoryClasses = getFactoryClasses();
        for (Class<? extends T> factoryClass : factoryClasses) {
            T instance = ReflectUtils.newInstance(factoryClass);
            instanceCache.add(instance);
        }
    }

    public Class<? extends T> [] getFactoryClasses() {
        if (classCache == null) {
            classCache = new ArrayList<>();
            for (String name : classNameCache) {
                classCache.add(ClassUtils.loadClass(classLoader, name));
            }
        }
        return classCache.toArray(new Class[0]);
    }

    public Class[] getFactoryClasses(ClassLoader classLoader) {
        List<Class> classList = new ArrayList<>();
        for (String name : classNameCache) {
            classList.add(ClassUtils.loadClass(classLoader, name));
        }
        return classList.toArray(new Class[0]);
    }

    public static <T> FactoryLoader<T> load(Class<T> factoryClass) {
        return load(factoryClass, ClassUtils.getDefaultClassLoader());
    }

    public static <T> FactoryLoader<T> load(Class<T> factoryClass, ClassLoader classLoader) {
        return load(factoryClass.getName(), classLoader);
    }

    public static <T> FactoryLoader<T> load(String factoryClassName) {
        return load(factoryClassName, ClassUtils.getDefaultClassLoader());
    }

    public static <T> FactoryLoader<T> load(String factoryClassName, ClassLoader classLoader) {

        if (FACTORY_CACHE == null) {
            try {
                loadCache(classLoader);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        if (!FACTORY_CACHE.containsKey(factoryClassName)) {
            return null;
        }
        FactoryLoader<T> factoryLoader = FACTORY_CACHE.get(factoryClassName);


        return factoryLoader;
    }

    private static void loadCache(ClassLoader classLoader) throws IOException {
        FACTORY_CACHE = new ConcurrentHashMap<>();
        Enumeration<URL> resources = classLoader.getResources(FACTORY_PATH);
        while (resources.hasMoreElements()) {
            doLoad(resources.nextElement(), classLoader);
        }
        //去重
        for (String interfacesName : FACTORY_CACHE.keySet()) {
            FactoryLoader factoryLoader = FACTORY_CACHE.get(interfacesName);
            List<String> oldList = factoryLoader.classNameCache;
            factoryLoader.classNameCache = ListUtils.deduplication(oldList);
        }
    }

    private static void doLoad(URL factoryURL, ClassLoader classLoader) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = factoryURL.openStream()) {
            properties.load(inputStream);
            Set<String> interfacesNames = properties.stringPropertyNames();
            for (String interfacesName : interfacesNames) {
                String[] factoryNameArray = properties.getProperty(interfacesName).split(",");
                List<String> factoryNames = ListUtils.array2List(factoryNameArray);
                if (FACTORY_CACHE.containsKey(interfacesName)) {

                    FACTORY_CACHE.get(interfacesName).classNameCache.addAll(factoryNames);
                } else {
                    FactoryLoader factoryLoader = new FactoryLoader<>(factoryNames, interfacesName, classLoader);
                    FACTORY_CACHE.put(interfacesName,factoryLoader);
                }
            }
        }
    }
}
