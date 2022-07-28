package com.kamo.context.annotation;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionBuilder;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.util.ReflectUtils;

import java.beans.Introspector;

public class ClassPathBeanDefinitionScanner extends AbstractScanner {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }


    public void register(Class beanClass) {
        Component component = ReflectUtils.getAnnotation(beanClass,Component.class);
        String beanName = component.value();
        if (beanName.equals("")) {
            beanName = Introspector.decapitalize(beanClass.getSimpleName());
        }
        BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(beanClass);
        registry.registerBeanDefinition(beanName, beanDefinition);

    }

    public boolean isRegisterClass(Class loaderClass) {

        return loaderClass != null
                && ReflectUtils.isAnnotationPresent(loaderClass,Component.class)
                && !loaderClass.isInterface();
    }
}



