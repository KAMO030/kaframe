package com.kamo.context.env.impl;

import java.util.HashMap;
import java.util.Map;

public class PropertyHolder {

    private final Map<String, Object> properties;
    private final String prefix;

    public PropertyHolder(String prefix) {
        this.prefix = prefix;
        this.properties = new HashMap<>();
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Object getProperty(String key, Object defaultValue) {
        return properties.containsKey(key) ? properties.get(key):defaultValue;
    }
    public boolean containsProperty(String suffix){
        return properties.containsKey(suffix);
    }

    public <T extends Object> Object setProperty(String suffix, T value) {
        return properties.put(suffix, value);
    }


}
