package com.kamo.context.annotation;


import com.kamo.context.BeanDefinition;
import com.kamo.context.Property;
import com.kamo.util.ReflectUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;


public class AutowiredPropertyResolve extends AbstractPropertyResolve {


    private BeanDefinition beanDefinitio;

    public AutowiredPropertyResolve(BeanDefinition beanDefinition) {
        this.beanDefinitio = beanDefinition;

    }

    @Override
    public void parse() {
        parseField(beanDefinitio.getBeanClass());
        parseMethod(beanDefinitio.getBeanClass());


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
        property.setLazed(ReflectUtils.isAnnotationPresent(writeMethod,Lazy.class));
        beanDefinitio.addProperty(property);
    }

    private void parseField(Class beanClass) {
        do {
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (needParse(field)) {
                    doParseField(field);
                }
            }
        } while (beanClass != null
                &&! beanClass.isInterface()
                && !(beanClass = beanClass.getSuperclass()).equals(Object.class));
    }

    protected void doParseField(Field field) {
        Property property = new Property(field);
        String value = ReflectUtils.getAnnotation(field, Autowired.class).value();
        if (!value.equals("")) {
            property.setValue(value);
        }
        property.setGenericType( field.getGenericType());
        property.setType(field.getType());
        property.setName(field.getName());
        property.setLazed(ReflectUtils.isAnnotationPresent(field,Lazy.class));
        beanDefinitio.addProperty(property);
    }


}
