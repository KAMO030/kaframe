package com.kamo.context.env.impl;

import com.kamo.context.annotation.Autowired;

import com.kamo.context.env.Environment;
import com.kamo.context.env.PropertyHolder;
import com.kamo.context.env.PropertyParser;
import com.kamo.context.env.PropertySource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEnvironment implements Environment {

    @Autowired
    private List<PropertyParser> propertyParserList;
    private final Map<String, PropertyHolder> propertyHolderMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void initialize(){
        if (propertyParserList == null) {
            propertyParserList = new ArrayList<>();
        }
    }
    @Override
    public boolean containsProperty(String key) {
        int prefixIndex = key.lastIndexOf('.');
        String suffix = prefixIndex == -1 ? key : key.substring(prefixIndex + 1);
        String prefix = prefixIndex == -1 ? "" : key.substring(0, prefixIndex);
        PropertyHolder propertyHolder = getPropertyHolder(prefix);
        if (propertyHolder == null) {
            return false;
        }
        return propertyHolder.containsProperty(suffix);
    }

    @Override
    public Object setProperty(String key, Object value) {
        int prefixIndex = key.lastIndexOf('.');
        String suffix = prefixIndex == -1 ? key : key.substring(prefixIndex + 1);
        String prefix = prefixIndex == -1 ? "" : key.substring(0, prefixIndex);
        if (propertyHolderMap.containsKey(prefix)) {
            return getPropertyHolder(prefix).setProperty(suffix, value);
        }
        PropertyHolder propertyHolder = new GenericPropertyHolder(prefix);
        propertyHolder.setProperty(suffix, value);
        propertyHolderMap.put(prefix, propertyHolder);
        return null;
    }

    @Override
    public Object getProperty(String key) {
        return getProperty(key, null, null);
    }

    @Override
    public Object getProperty(String key, Object defaultValue) {
        return getProperty(key, null, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return getProperty(key, targetType, null);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        int prefixIndex = key.lastIndexOf('.');
        String prefix = prefixIndex == -1 ? "" : key.substring(0, prefixIndex);
        PropertyHolder propertyHolder = getPropertyHolder(prefix);
        if (propertyHolder == null) {
            return defaultValue;
        }
        String suffix = prefixIndex == -1 ? key : key.substring(prefixIndex + 1);
        return propertyHolder.getProperty(suffix,targetType,defaultValue);
    }

    @Override
    public void registerPropertyParser(PropertyParser propertyParser) {
        propertyParserList.add(propertyParser);
    }

    private PropertyHolder getPropertyHolder(String prefix) {
        return propertyHolderMap.get(prefix);
    }

    @Override
    public void parse(PropertySource propertySource) {
        Class sourceClass = propertySource.getClass();
        for (PropertyParser propertyParser : propertyParserList) {
            if (propertyParser.propertySourceType().equals(sourceClass)) {
                propertyParser.parse(propertySource);
                return;
            }
        }
        throw new RuntimeException("无法解析 " + sourceClass.getName() + "类型的数据源");
    }
}
