package com.kamo.context.annotation.support;

import com.kamo.bean.annotation.Bean;
import com.kamo.bean.annotation.Configuration;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.bean.BeanDefinition;
import com.kamo.core.cglib.MethodProxy;
import com.kamo.core.cglib.ProxyClass;
import com.kamo.core.util.AnnotationUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class AnnotationConfigParser {
    public static void parseConfiguration(ApplicationContext context, ConditionMatcher conditionMatcher, BeanDefinition configBeanDefinition) {
        Class configClass = configBeanDefinition.getBeanClass();
        Configuration annotation = AnnotationUtils.getAnnotation(configClass, Configuration.class);
        if (annotation.proxyBeanMethods()) {
            crateProxyClass(context, configBeanDefinition);
        }
        new ConfigurationClassResolve(context, conditionMatcher, configClass)
                .parse();
        new ConfigurationAnnotationResolve(context, conditionMatcher, configClass)
                .parse();
    }

    private static void crateProxyClass(ApplicationContext context, BeanDefinition configBeanDefinition) {
        configBeanDefinition.setInstanceSupplier(() ->
                ProxyClass.newProxyInstance(configBeanDefinition.getBeanClass(), new ConfigurationHandler(context)
                ));
    }

    private static class ConfigurationHandler implements InvocationHandler {
        private ApplicationContext context;
        public ConfigurationHandler(ApplicationContext context) {
            this.context = context;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (method.isAnnotationPresent(Bean.class)) {
                String name = method.getAnnotation(Bean.class).name();
                name = name.equals("") ? methodName : name;
                if (context.containSingletonBean(name)) {
                    return context.getBean(name);
                }
                Object result = MethodProxy.invokeSuper(proxy, method, args);
                context.addSingletonBean(name, result);
                return result;
            }
            return MethodProxy.invoke(proxy, method, args);
        }
    }

}
