package com.kamo.context.annotation.support;


import com.kamo.bean.annotation.Autowired;
import com.kamo.bean.annotation.Lazy;
import com.kamo.bean.support.Property;
import com.kamo.bean.BeanDefinition;
import com.kamo.core.util.AnnotationUtils;
import com.kamo.core.util.ReflectUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


public class AutowiredPropertyResolver extends AbstractPropertyResolver {


    private BeanDefinition beanDefinition;

    public AutowiredPropertyResolver(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;

    }

    @Override
    public void parse() {
        Class beanClass = beanDefinition.getBeanClass();
        parseField(beanClass);
        parseMethod(beanClass);
        parseConstructor(beanClass);
    }

    private void parseMethod(Class beanClass) {
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                doParseSetMethod(propertyDescriptor);
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    private void doParseSetMethod(PropertyDescriptor propertyDescriptor) {
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Autowired autowired = writeMethod.getAnnotation(Autowired.class);
        if (autowired == null) {
            return;
        }
        Property property = new Property(writeMethod);
        String value = autowired.value();
        if (!value.equals("")) {
            property.setValue(value);
        }
        Parameter parameter = writeMethod.getParameters()[0];
        property.setGenericType( parameter.getParameterizedType());
        property.setName(propertyDescriptor.getName());
        property.setType(parameter.getType());
        property.setLazed(AnnotationUtils.isAnnotationPresent(writeMethod, Lazy.class));
        beanDefinition.addProperty(property);
    }

    private void parseField(Class beanClass) {
        ReflectUtils.forEachField(beanClass,  field -> {
            if (needParse(field)) {
                doParseField(field);
            }
            return false;
        });
//        do {
//            Field[] declaredFields = beanClass.getDeclaredFields();
//            for (Field field : declaredFields) {
//                if (needParse(field)) {
//                    doParseField(field);
//                }
//            }
//        } while (beanClass != null
//                && !(beanClass = beanClass.getSuperclass()).equals(Object.class));
    }

    protected void doParseField(Field field) {
        Property property = new Property(field);
        String value = AnnotationUtils.getAnnotation(field, Autowired.class).value();
        if (!value.equals("")) {
            property.setValue(value);
        }
        property.setGenericType( field.getGenericType());
        property.setType(field.getType());
        property.setName(field.getName());
        property.setLazed(AnnotationUtils.isAnnotationPresent(field,Lazy.class));
        beanDefinition.addProperty(property);
    }


    private void parseConstructor(Class beanClass) {
        for (Constructor constructor : beanClass.getConstructors()) {
            if (AnnotationUtils.isAnnotationPresent(constructor, Autowired.class)) {
                Parameter[] parameters = constructor.getParameters();
                beanDefinition.setArguments(parameters);
                break;
            }
        }
    }

}
