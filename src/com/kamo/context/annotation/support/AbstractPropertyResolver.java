package com.kamo.context.annotation.support;


import com.kamo.bean.annotation.Autowired;
import com.kamo.context.annotation.PropertyResolver;
import com.kamo.core.util.AnnotationUtils;

import java.lang.reflect.AnnotatedElement;

public abstract class AbstractPropertyResolver implements PropertyResolver {

    public boolean needParse(AnnotatedElement element) {
        return AnnotationUtils.isAnnotationPresent(element, Autowired.class);
    }
}
