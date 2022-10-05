package com.kamo.core.cglib;

import com.kamo.core.cglib.asm.AsmProxyClassWriter;
import com.kamo.core.cglib.asm.AsmUtil;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.kamo.core.cglib.ProxyClass.getProxyClassName;

public final class ProxyClassLoader extends ClassLoader {
        private final Map<String, Class> proxyClassCache = new ConcurrentHashMap<>();

        ProxyClassLoader(ClassLoader parent) {
            super(parent);
        }

        Class getProxyClass(Class superclass, boolean isWriteFile) {
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
                URL resource = this.getResource(superClassFile);
                Objects.requireNonNull(resource);
                String filePath = resource.getFile();
                AsmUtil.writeBytes(filePath + "/" + proxyClassFile, cacheBytes);
            }

            Class<?> proxyClass = defineClass(cacheKey, cacheBytes, 0, cacheBytes.length);
            proxyClassCache.put(cacheKey, proxyClass);
            return proxyClass;
        }

    }