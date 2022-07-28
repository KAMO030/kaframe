package com.kamo.context.annotation;

import com.kamo.context.Resolve;

import java.lang.reflect.AnnotatedElement;


public interface PropertyResolve extends Resolve {
     boolean needParse(AnnotatedElement element);
}
