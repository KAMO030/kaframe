package com.kamo.context.annotation;

import com.kamo.context.Arguments;
import com.kamo.context.GenericBeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodBeanDefinition extends GenericBeanDefinition {
    private Object configBean;
    private Method instanceMethod;

    public MethodBeanDefinition(Object configBean, Method instanceMethod) {
        this.configBean = configBean;
        this.instanceMethod = instanceMethod;
        init();

    }

    private void init() {
        setBeanClass(instanceMethod.getReturnType());
        setLazyInit(instanceMethod.isAnnotationPresent(Lazy.class));
        setScope(instanceMethod.isAnnotationPresent(Scope.class)
                ?instanceMethod.getAnnotation(Scope.class).value()
                :Scope.SINGLETON);
        if (instanceMethod.getParameterCount()>0) {
            Parameter[] parameters = instanceMethod.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Arguments args = new Arguments(parameters[i]);
                addArguments(args.getName(),args );
            }
        }
    }

    @Override
    public Object doInstance(Object[] args) throws InvocationTargetException, IllegalAccessException {
        return instanceMethod.invoke(configBean,args);
    }
}
