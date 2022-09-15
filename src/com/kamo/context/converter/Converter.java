package com.kamo.context.converter;

import com.kamo.context.factory.ApplicationProcessor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 转换器
 * @param <T> value原本的类型
 * @param <R> 需要转换到的类型
 */
@FunctionalInterface
public interface Converter<T,R>  {
    R convert(T value);
    default Class<R> getType(){
        Type[] types =  this.getClass().getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                return (Class<R>) parameterizedType.getActualTypeArguments()[1];
            }
        }
        throw new IllegalArgumentException("泛型异常");
    };
}
