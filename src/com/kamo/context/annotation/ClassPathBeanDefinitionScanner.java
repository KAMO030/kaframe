package com.kamo.context.annotation;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionRegistry;


import java.beans.Introspector;

public class ClassPathBeanDefinitionScanner extends AbstractScanner {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }


    public void register(Class beanClass) {
        Component component = (Component) beanClass.getAnnotation(Component.class);
        String beanName = component.value();
        if (beanName.equals("")) {
            beanName = Introspector.decapitalize(beanClass.getSimpleName());
        }
        BeanDefinition beanDefinition = new ScannedGenericBeanDefinition();
        if (beanClass.isAnnotationPresent(Scope.class)) {
            Scope scope = (Scope) beanClass.getAnnotation(Scope.class);
            beanDefinition.setScope(scope.value());
        } else {
            beanDefinition.setScope(Scope.SINGLETON);
        }
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(beanClass.isAnnotationPresent(Lazy.class));
        AutowiredPropertyResolve propertyResolve = new AutowiredPropertyResolve(beanDefinition);
        propertyResolve.parse();
        registry.registerBeanDefinition(beanName, beanDefinition);

    }

    public boolean isRegisterClass(Class loaderClass) {

        return loaderClass != null
                && loaderClass.isAnnotationPresent(Component.class)
                && !loaderClass.isInterface();
    }
}



