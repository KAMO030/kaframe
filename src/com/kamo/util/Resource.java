package com.kamo.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public abstract class Resource {
    public static URL getResource(String resourcePath){
        return Resource.class.getClassLoader().getResource(resourcePath);
    }
    public static InputStream getResourceAsStream(String resourcePath){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
    }
    public static Reader getResourceAsReader(String resourcePath){
        return new InputStreamReader(getResourceAsStream(resourcePath));
    }
}
