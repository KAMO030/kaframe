package com.kamo.context;

import com.kamo.util.ReflectUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class FactoryLoader {
    private static Map<String,List<String>> FACTORY_CACHE ;
    private static final String FACTORY_PATH = "META-INF/kamo.factories";
    private FactoryLoader(){};

    public static <T> Class<T>[] load(Class<T> factoryClass){
        return load(factoryClass,Thread.currentThread().getContextClassLoader());
    }
    public static <T> Class<T>[] load(Class<T> factoryClass, ClassLoader contextClassLoader){
        String className = factoryClass.getName();
        if (FACTORY_CACHE == null) {
            try {
                loadCache(contextClassLoader);
            } catch (IOException e) {
                e.printStackTrace();
                return new Class[0];
            }
        }
        if (!FACTORY_CACHE.containsKey(className)) {
            return new Class[0];
        }
        List<String> classNames = FACTORY_CACHE.get(className);
        List<Class<T>> classList= new ArrayList<>();
        for (String name : classNames) {
            classList.add(ReflectUtil.loadClass(contextClassLoader,name)) ;
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
            List<String> newList = new ArrayList<>(new HashSet<>(oldList));
            FACTORY_CACHE.replace(interfacesName,oldList,newList);
        }
    }

    private static void doLoad(URL factoryURL) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = factoryURL.openStream()){
            properties.load(inputStream);
            Set<String> interfacesNames = properties.stringPropertyNames();
            for (String interfacesName : interfacesNames) {
                String[] factoryNameArray = properties.getProperty(interfacesName).split(",");
                List<String> factoryNames = new ArrayList<>();
                for (String factoryName : factoryNameArray) {
                    factoryNames.add(factoryName.trim());
                }
                if (FACTORY_CACHE.containsKey(interfacesName)) {
                    FACTORY_CACHE.get(interfacesName).addAll(factoryNames);
                }else {
                    FACTORY_CACHE.put(interfacesName,factoryNames);
                }
            }
        }
    }
}
