package com.kamo.core.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public final class ResourceUtils {
    public static URL getResource(String resourcePath){
        return ResourceUtils.class.getClassLoader().getResource(resourcePath);
    }
    public static InputStream getResourceAsStream(String resourcePath){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
    }
    public static Reader getResourceAsReader(String resourcePath){
        return new InputStreamReader(getResourceAsStream(resourcePath));
    }
}
