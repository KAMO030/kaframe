package com.kamo.util;


import javafx.util.converter.DateStringConverter;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ConverterRegistry {
    private static Map<Class, Set<Converter>> converterMap = new ConcurrentHashMap<>();

    private ConverterRegistry() {
        throw new RuntimeException();
    }

    static {
//        registerConverter(String[].class, value -> value[0]);
//        registerConverter(String[].class, values -> values);
//        registerConverter(String[].class, values -> Boolean.parseBoolean(values[0]));
//        registerConverter(String[].class, values -> new DateStringConverter(DateFormat.DEFAULT).fromString( values[0]));
//        registerConverter(String[].class,  values -> Integer.valueOf(values[0]));
        registerConverter(new StringDateConverter());
    }
    public static boolean isRegister(Class returnType,Class targetType){
        return getConverter(returnType,targetType)!=null;
    }
    public static <T, R> Converter<T,R> getConverter(Class<R> returnType, Class<T> targetType){
        if (!converterMap.containsKey(targetType)) {
            return null;
        }
        Set<Converter> converterSet = converterMap.get(targetType);
        for (Converter converter : converterSet) {
            if (converter.getType().isAssignableFrom(returnType)) {
                return converter;
            }
        }
        return null;
    }
    /**
     * @param target     需要转换的对象实例
     * @param returnType 需要转换到的类型
     * @param <T>        转换后的类型
     * @param <R>        转换前的类型
     *                   如果传入的 #target 是数组而 #returnType 的类型不是数组则只会转换 #target 的第一个元素
     *                   如果传入的 #target 不是数组类型但 #returnType 是数组类型 时可能抛出无法转换异常
     *                   如果传入的 #target 是数组而 #returnType 的类型也是数组则会把 #target 数组的每一个元素转换成 #returnType 数组类型的具体类型然后封装到一个 #returnType类型的数组里返回
     * @return 转换后的对象实例
     * @throws IllegalArgumentException 当没有对应支持的转换器时抛出
     */
    public static <T, R> R convert(T target, Class<R> returnType) {
        Class targetType = target.getClass();
        if (targetType.isArray()) {
            return returnType.isArray() ?
                    (R) doConvertArray((T[]) target, (Class<R[]>) returnType, targetType) :
                    (R) doConvert(Array.get(target, 0), returnType, targetType.getComponentType());
        }
        return (R) doConvert(target, returnType, targetType);
    }

    public static <T, R> R[] doConvertArray(T[] target, Class<R[]> returnType, Class<T[]> targetType) {

        List partList = new ArrayList<>();
        Class<R> returnComponentType = (Class<R>) returnType.getComponentType();
        Class<T> targetComponentType = (Class<T>) targetType.getComponentType();
        try {
            int i = 0;
            while (true) {
                T component = (T) Array.get(target, i++);
                R convertObj = doConvert(component, returnComponentType, targetComponentType);
                partList.add(convertObj);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return (R[]) partList.toArray();
    }

    public static <T, R> R doConvert(T target, Class<R> returnType, Class<T> targetType) {
        if (ReflectUtils.isPrimitive(returnType) && targetType.equals(String.class)) {
            try {
                return (R) (returnType.equals(String.class) ?
                        target.toString() :
                        returnType.getMethod("valueOf", String.class).invoke(null, target.toString()));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {}
        }
        returnType = ReflectUtils.getWrapperClass(returnType);
        targetType = ReflectUtils.getWrapperClass(targetType);
        Converter<T, R> converter = getConverter(returnType, targetType);
        if (converter != null) {
           return converter.convert(target);
        }
        if (returnType.equals(String.class)) {
            return (R) target.toString();
        }
        throw new IllegalArgumentException("无法将: " + targetType + " 类型转换到: " + returnType + " 类型");
    }

    public static <R, T> void registerConverter(Converter<T, R> converter) {
        Class<R> targetType =(Class<R>) ((ParameterizedType) converter.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        Set converterSet;
        if (converterMap.containsKey(targetType)) {
            converterSet = converterMap.get(targetType);
        } else {
            converterSet = new HashSet();
            converterMap.put(targetType, converterSet);
        }
        converterSet.add(converter);
    }


    public static class StringDateConverter implements Converter<String, Date> {
        @Override
        public Date convert(String value) {
            return new DateStringConverter(DateFormat.DEFAULT).fromString(value);
        }
    }
}
