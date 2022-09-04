package com.kamo.context;

import com.kamo.util.ReflectUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface Aware{
    void setAware(Object... awareValue);
    Class[] getAwareTypes();

    default String[] getAwareNames(){
        Class[] awareTypes = getAwareTypes();
        String[] awareNames = new String[awareTypes.length];
        for (int i = 0; i < awareTypes.length; i++) {
            awareNames[i] = awareTypes[i].getSimpleName();
        }
        return awareNames;
    };
}
