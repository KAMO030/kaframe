package com.kamo.util;

/**
 * 转换器
 * @param <T> 需要从String数组转换到的类型
 */
@FunctionalInterface
public interface Converter<T> {
    T convert(Object... values);
}
