package com.kamo.bean.support;

import java.lang.reflect.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Property<T> {
    private String name;
    private Object value;

    private Class<T> type;

    private Supplier<T> propertyGetter;
    private Object bean;
    private Consumer<T> propertySetter;

    private AnnotatedElement annotatedElement;
    private Type genericType;
    private Class valueType;
    private boolean isLazed;
    public Property(Field field){
        annotatedElement = field;
        propertySetter = (value) -> {
            try {
                field.setAccessible(true);
                field.set(bean,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getCause());
            }
        };
        propertyGetter = () -> {
            try {
                field.setAccessible(true);
                return (T) field.get(bean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

    }

    public Type getGenericType() {
        return genericType;
    }

    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }

    public Property(Method method){
        annotatedElement = method;
        propertySetter = (value) -> {
            try {
                method.setAccessible(true);
                method.invoke(bean, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getCause());
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        };

    }


    public void assignPro(Object value) {
        this.propertySetter.accept((T) value);
    }
    public boolean isNeedAssign(){
        return propertyGetter == null ? true : propertyGetter.get() == null;
    }
    public void setBean(Object bean) {
        this.bean = bean;
    }

    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }
    public Class getValueType() {
        return  valueType!=null ?valueType:
                value==null?null :value.getClass();
    }
    public void setValue(Object value) {
        this.value = value;
    }


    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public boolean isLazed() {
        return isLazed;
    }

    public void setLazed(boolean lazed) {
        isLazed = lazed;
    }

}
