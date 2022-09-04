package com.kamo.context.env;

import com.kamo.context.annotation.PropertyResolver;


public interface Environment extends PropertyParser{
    boolean containsProperty(String key);

    Object setProperty(String key, Object defaultValue);
    Object getProperty(String key);

    Object getProperty(String key, Object defaultValue);

    <T> T getProperty(String key, Class<T> targetType);

    <T> T getProperty(String key, Class<T> targetType, T defaultValue);

}