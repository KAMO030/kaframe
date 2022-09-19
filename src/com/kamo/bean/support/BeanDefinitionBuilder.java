package com.kamo.bean.support;

import com.kamo.bean.annotation.Destroy;
import com.kamo.bean.annotation.Lazy;
import com.kamo.bean.annotation.Scope;
import com.kamo.context.annotation.support.AnnotationBeanDefinition;
import com.kamo.context.annotation.support.AutowiredPropertyResolver;
import com.kamo.bean.BeanDefinition;
import com.kamo.core.util.AnnotationUtils;
import com.kamo.core.util.ReflectUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class BeanDefinitionBuilder {
    public static BeanDefinition getBeanDefinition(Class beanClass){
        return getBeanDefinition(beanClass,null);
    }
    public static BeanDefinition getBeanDefinition(Class beanClass,Supplier instanceSupplier){
        BeanDefinition beanDefinition = new AnnotationBeanDefinition();
        String scope = beanClass.isAnnotationPresent(Scope.class)?
                        Scope.SINGLETON.equalsIgnoreCase(((Scope)beanClass.getAnnotation(Scope.class)).value())?
                                Scope.SINGLETON:
                                Scope.PROTOTYPE:
                        Scope.SINGLETON;
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setScope(scope);
        beanDefinition.setInstanceSupplier(instanceSupplier);
        beanDefinition.setLazyInit(beanClass.isAnnotationPresent(Lazy.class));
        new AutowiredPropertyResolver(beanDefinition).parse();
        parseLifeMethod(beanDefinition,beanClass);

        return beanDefinition;
    }

    public static BeanDefinition getBeanDefinition(Method beanMethod,Supplier instanceSupplier){
        BeanDefinition beanDefinition = new MethodBeanDefinition(beanMethod);
        beanDefinition.setBeanClass(beanMethod.getReturnType());
        beanDefinition.setLazyInit(beanMethod.isAnnotationPresent(Lazy.class));
        beanDefinition.setScope(beanMethod.isAnnotationPresent(Scope.class) ?beanMethod.getAnnotation(Scope.class).value() :Scope.SINGLETON);
        beanDefinition.setInstanceSupplier(instanceSupplier);
        new AutowiredPropertyResolver(beanDefinition).parse();
        if (beanMethod.getParameterCount()>0) {
            Parameter[] parameters = beanMethod.getParameters();
            beanDefinition.setArguments(parameters);
        }
        parseLifeMethod(beanDefinition,beanDefinition.getBeanClass());
        return beanDefinition;
    }

    private static void parseLifeMethod(BeanDefinition beanDefinition, Class beanClass) {
        AtomicReference<String> destroy = new AtomicReference<>();
        AtomicReference<String> postConstruct = new AtomicReference<>();
        ReflectUtils.forEachMethod(beanClass, method -> {
            if (AnnotationUtils.isAnnotationPresent(method, Destroy.class)) {
                destroy.set(method.getName());
            }else if (AnnotationUtils.isAnnotationPresent(method, PostConstruct.class)){
                postConstruct.set(method.getName());
            }
            return false;
        });

        String destroyMethodName = destroy.get();
        String initMethodName = postConstruct.get();
        if (destroyMethodName != null) {
            beanDefinition.setDestroyMethodName(destroyMethodName);
        }
        if (initMethodName != null) {
            beanDefinition.setInitMethodName(initMethodName);
        }
    }
    public static BeanDefinition getBeanDefinition(Method beanMethod){
       return getBeanDefinition(beanMethod,null);
    }
}
