package com.kamo.context;

import com.kamo.context.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.Supplier;

public class BeanDefinitionBuilder {
    public static BeanDefinition getBeanDefinition(Class beanClass){
        return getBeanDefinition(beanClass,null);
    }
    public static BeanDefinition getBeanDefinition(Class beanClass,Supplier instanceSupplier){
        BeanDefinition beanDefinition = new AnnotationBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        String scope =
                beanClass.isAnnotationPresent(Scope.class)?
                Scope.SINGLETON.equalsIgnoreCase(((Scope)beanClass.getAnnotation(Scope.class)).value())?
                        Scope.SINGLETON:
                        Scope.PROTOTYPE:
                Scope.SINGLETON;
        beanDefinition.setScope(scope);
        beanDefinition.setInstanceSupplier(instanceSupplier);
        beanDefinition.setLazyInit(beanClass.isAnnotationPresent(Lazy.class));
        AutowiredPropertyResolve propertyResolve = new AutowiredPropertyResolve(beanDefinition);
        propertyResolve.parse();
        return beanDefinition;
    }
    public static BeanDefinition getBeanDefinition(Method beanMethod){
       return getBeanDefinition(beanMethod,null);
    }
    public static BeanDefinition getBeanDefinition(Method beanMethod,Supplier instanceSupplier){
        BeanDefinition beanDefinition = new MethodBeanDefinition(beanMethod);
        beanDefinition.setBeanClass(beanMethod.getReturnType());
        beanDefinition.setLazyInit(beanMethod.isAnnotationPresent(Lazy.class));
        beanDefinition.setScope(beanMethod.isAnnotationPresent(Scope.class)
                ?beanMethod.getAnnotation(Scope.class).value()
                :Scope.SINGLETON);
        beanDefinition.setInstanceSupplier(instanceSupplier);
        new AutowiredPropertyResolve(beanDefinition).parse();
        if (beanMethod.getParameterCount()>0) {
            Parameter[] parameters = beanMethod.getParameters();
            beanDefinition.setArguments(parameters);
        }
        return beanDefinition;
    }
}
