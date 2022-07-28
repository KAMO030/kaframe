package com.kamo.context.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanDefinition extends AnnotationBeanDefinition {

    private Object configBean;
    private Method instanceMethod;


    public MethodBeanDefinition(Method instanceMethod) {
        this.instanceMethod = instanceMethod;
    }


    @Override
    public Object doInstance(Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (configBean == null) {
            configBean = instanceSupplier.get();
        }
        return instanceMethod.invoke(configBean,args);
    }
}
