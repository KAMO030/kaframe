package com.kamo.context.annotation;

import com.kamo.context.factory.Resolver;

import java.lang.reflect.AnnotatedElement;


public interface PropertyResolver extends Resolver {
     boolean needParse(AnnotatedElement element);
}
