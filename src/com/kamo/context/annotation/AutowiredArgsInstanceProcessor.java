package com.kamo.context.annotation;

import com.kamo.context.BeanDefinition;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.util.AnnotationUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class AutowiredArgsInstanceProcessor implements BeanInstanceProcessor {
    @Override
    public Object instanceBefore(String beanName, BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();
        for (Constructor constructor : beanClass.getConstructors()) {
            if (AnnotationUtils.isAnnotationPresent(constructor, Autowired.class)) {
                Parameter[] parameters = constructor.getParameters();
                beanDefinition.setArguments(parameters);
                break;
            }
        }
        return null;
    }

}
