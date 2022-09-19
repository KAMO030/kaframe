package com.kamo.context.annotation.support;

import com.kamo.bean.annotation.Autowired;
import com.kamo.bean.annotation.Order;
import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.core.util.AnnotationUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
@Order(1)
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
