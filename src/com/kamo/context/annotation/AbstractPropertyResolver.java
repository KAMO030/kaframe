package com.kamo.context.annotation;


import com.kamo.util.AnnotationUtils;

import java.lang.reflect.AnnotatedElement;

public abstract class AbstractPropertyResolver implements PropertyResolver {

    public boolean needParse(AnnotatedElement element) {
        return AnnotationUtils.isAnnotationPresent(element,Autowired.class);
    }
}
