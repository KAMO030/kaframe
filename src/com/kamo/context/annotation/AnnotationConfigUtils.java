package com.kamo.context.annotation;

import com.kamo.cglib.MethodProxy;
import com.kamo.cglib.ProxyClass;
import com.kamo.context.ApplicationContext;
import com.kamo.context.BeanDefinition;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.util.AnnotationUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class AnnotationConfigUtils {
    public static void parseConfiguration(ApplicationContext context, ConditionMatcher conditionMatcher, BeanDefinition configBeanDefinition) {
        Class configClass = configBeanDefinition.getBeanClass();
        Configuration annotation = AnnotationUtils.getAnnotation(configClass, Configuration.class);
        if (annotation.isProxyMethod()) {
            crateProxyClass(context,configBeanDefinition);
        }
        new ConfigurationClassResolve(context, conditionMatcher, configClass)
                .parse();
        new ConfigurationAnnotationResolve( context, conditionMatcher,configClass)
                .parse();
    }

    private static void crateProxyClass(ApplicationContext context, BeanDefinition configBeanDefinition) {
        Class configClass = configBeanDefinition.getBeanClass();

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        configBeanDefinition.setInstanceSupplier(() -> ProxyClass.newProxyInstance(contextClassLoader, configClass, true,new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                if (method.isAnnotationPresent(Bean.class)) {
                    String name = method.getAnnotation(Bean.class).name();
                    name = name.equals("") ? methodName : name;
                    if (context.containSingletonBean(name)) {
                        return context.getBean(name);
                    }
                    Object result = MethodProxy.invokeSuper(proxy,method,args);
                    context.addSingletonBean(name,result);
                    return result;
                }
                return MethodProxy.invoke(proxy,method,args);
            }
        }));
    }

}
