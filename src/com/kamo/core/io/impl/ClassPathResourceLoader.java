package com.kamo.core.io.impl;


import com.kamo.core.io.ResourceHolder;
import com.kamo.core.io.ResourceLoader;
import com.kamo.core.util.StringUtils;

import java.util.Objects;

/**
 * 默认的资源加载器
 */
public class ClassPathResourceLoader implements ResourceLoader {
    private static final String CLASSPATH_URL_PREFIX = "classpath:";
    @Override
    public ResourceHolder getResourceHolder(String location) {
        Objects.requireNonNull(location, "Location must not be null");
        String classLocation = StringUtils.subStringOnAfter(location, CLASSPATH_URL_PREFIX);
        return new ClassPathResourceHolder(classLocation);
    }

    @Override
    public boolean isMatchLoader(String location) {
        return location.toLowerCase().startsWith(CLASSPATH_URL_PREFIX);
    }


}
