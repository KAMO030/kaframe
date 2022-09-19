package com.kamo.core.util;

import java.util.Arrays;
import java.util.Objects;

public final class ObjectUtils {
    private ObjectUtils(){}
    public static void requireNonNull(Object...objects){
        Arrays.stream(objects).forEach(o->Objects.requireNonNull(o));
    }
    public static void requireNonNull(String message ,Object...objects){
        Arrays.stream(objects).forEach(o->Objects.requireNonNull(o,message));
    }
    public static boolean isNotNull(Object...objects){
        return Arrays.stream(objects).allMatch(o -> Objects.nonNull(o));
    }

}
