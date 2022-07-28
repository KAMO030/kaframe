package com.kamo.context.annotation;


import com.kamo.util.ReflectUtils;

import java.lang.reflect.AnnotatedElement;

public abstract class AbstractPropertyResolve implements PropertyResolve {

    public boolean needParse(AnnotatedElement element) {
        return ReflectUtils.isAnnotationPresent(element,Autowired.class);
    }
}
