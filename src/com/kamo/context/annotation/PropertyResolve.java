package com.kamo.context.annotation;

import com.kamo.context.Resolve;

import java.lang.reflect.Field;


public interface PropertyResolve extends Resolve {
    boolean needParse(Field field);
}
