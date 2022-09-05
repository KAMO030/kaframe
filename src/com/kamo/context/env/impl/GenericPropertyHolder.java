package com.kamo.context.env.impl;

import com.kamo.context.env.PropertyHolder;

import java.util.HashMap;
import java.util.Map;

public class GenericPropertyHolder implements PropertyHolder {

    private final Map<String, Object> properties;
    private final String prefix;

    public GenericPropertyHolder(String prefix) {
        this.prefix = prefix;
        this.properties = new HashMap<>();
    }

    public Object getProperty(String suffix) {
        return getProperty(suffix,null,null);
    }

    public Object getProperty(String suffix, Object defaultValue) {
        return getProperty(suffix,null,defaultValue);
    }

    @Override
    public <T> T getProperty(String suffix, Class<T> targetType) {
        return getProperty(suffix,targetType,null);
    }

    @Override
    public <T> T getProperty(String suffix, Class<T> targetType, T defaultValue) {
        return !properties.containsKey(suffix) ? defaultValue : (T) properties.get(suffix);
    }

    public boolean containsProperty(String suffix){
        return properties.containsKey(suffix);
    }

    public  Object setProperty(String suffix, Object value) {
        return properties.put(suffix, value);
    }


}
