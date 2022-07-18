package com.kamo.util;

import java.lang.reflect.Field;

public final class ReflectUtils {
    private ReflectUtils() {

    }
    public static boolean isPrimitive(Class type){
        if (type.isPrimitive()){
            return true;
        }
        try {
            Field field = type.getDeclaredField("TYPE");
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
