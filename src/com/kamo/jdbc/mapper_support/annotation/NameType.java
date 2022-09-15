package com.kamo.jdbc.mapper_support.annotation;

import com.kamo.util.BeanUtil;
import com.kamo.context.converter.Converter;

public enum NameType {


    SAME((name)-> name),SNAKE_CASE((name)-> BeanUtil.toTableName((String) name));
    private final Converter<String,String> converter;

    NameType(Converter<String,String> converter) {
        this.converter = converter;
    }

    public Converter<String,String> getConverter() {
        return converter;
    }
}
