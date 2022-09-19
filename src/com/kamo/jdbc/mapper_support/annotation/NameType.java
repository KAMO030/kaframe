package com.kamo.jdbc.mapper_support.annotation;

import com.kamo.context.converter.Converter;
import com.kamo.core.util.BeanUtils;

public enum NameType {


    SAME((name)-> name),SNAKE_CASE((name)-> BeanUtils.toTableName((String) name));
    private final Converter<String,String> converter;

    NameType(Converter<String,String> converter) {
        this.converter = converter;
    }

    public Converter<String,String> getConverter() {
        return converter;
    }
}
