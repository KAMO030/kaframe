package com.kamo.util;

import java.lang.reflect.ParameterizedType;

/**
 * 转换器
 * @param <T> 需要从String数组转换到的类型
 */
@FunctionalInterface
public interface Converter<T,R> {
    R convert(T value);
    default Class<R> getType(){
        return (Class<R>) ((ParameterizedType) this.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
    };
}
