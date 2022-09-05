package com.kamo.context.listener.impl;

import com.kamo.context.annotation.Arg;
import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.ApplicationListener;
import com.kamo.util.ReflectUtil;
import com.kamo.util.exception.ReflectException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ApplicationListenerAdapter<E extends ApplicationEvent> implements ApplicationListener<E> {
    private Object source;
    private Method method;
    private String[] multicasterNames;
    private boolean isEventParameter;
    private Class<E> eventType;

    private  List<Field> parameterMappings;

    public ApplicationListenerAdapter(Object source, Method method, String[] multicasterNames) {
        this(source, method, multicasterNames, null);
    }

    public ApplicationListenerAdapter(Object source, Method method, String[] multicasterNames, Class<E> eventType) {
        this.source = source;
        this.method = method;
        this.multicasterNames = multicasterNames;
        this.eventType = eventType;
        this.isEventParameter = method.getParameterCount() == 1 && ApplicationEvent.class.isAssignableFrom(method.getParameterTypes()[0]);
        if (!isEventParameter) {
            this.parameterMappings = getParameterMappings();
        }
    }

    private  List<Field> getParameterMappings() {
        Class eventType = getEventType();
        Parameter[] parameters = method.getParameters();
        List<Field>  parameterMappings = new ArrayList<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            String name = parameters[i].getName();
            Class type = parameters[i].getType();
            if (eventType.isAssignableFrom(type)) {
                parameterMappings.add(null);
                continue;
            }
            if (parameters[i].isAnnotationPresent(Arg.class)) {
                String argName = parameters[i].getAnnotation(Arg.class).name();
                name = argName.equals("") ? name : argName;
            }
            String finalName = name;
            ReflectUtil.forEachField(eventType, field -> {
                Class<?> fieldType =  field.getType();
                String fieldName = field.getName();
                //如果自定义了名字，将只会按名字去找
                if (!finalName.startsWith("arg") ) {
                    if (!finalName.equals(fieldName)) {
                        return false;
                    }
                    if (!(fieldType.isAssignableFrom(type) || type.isAssignableFrom(fieldType))) {
                        throw new IllegalArgumentException(method + "监听器的 " + finalName + " 属性类型转换异常");
                    }
                    parameterMappings.add(field);
                    return true;
                } else if ((fieldType.isAssignableFrom(type) || type.isAssignableFrom(fieldType))) {
                    //如果没有自定义名字，将只会按照类型去找，有多个类型的属性匹配时默认使用除source外第一个
                    if (fieldType.equals(Object.class) && fieldName.equals("source")) {
                        throw new IllegalArgumentException("找不到: " + method + "监听器的 " + finalName + " 属性");
                    }
                    parameterMappings.add(field);
                    return true;
                }
                return false;
            });
        }
        return parameterMappings;
    }

    @Override
    public String[] getMulticasterNames() {
        return multicasterNames == null || multicasterNames.length == 0 ?
                ApplicationListener.super.getMulticasterNames() : multicasterNames;
    }

    @Override
    public void onApplicationEvent(E event) {
        Object[] args = getArgsFromEvent(event);
        try {
            ReflectUtil.invokeMethod(method, source, args);
        } catch (ReflectException e) {
            e.printStackTrace();
        }
    }

    protected Object[] getArgsFromEvent(E event) {

        if (isEventParameter) {
            return new Object[]{event};
        }

        Object[] args = new Object[parameterMappings.size()];
        for (int i = 0; i < parameterMappings.size(); i++) {
            Field field = parameterMappings.get(i);
            if (field == null) {
                args [i] = event;
                continue;
            }
            args [i] = ReflectUtil.getFieldValue(field,event);
        }
        return args;
    }

    @Override
    public Class getEventType() {
        if (!eventType.equals(ApplicationEvent.class)) {
            return eventType;
        }
        try {
            return ReflectUtil.getActualTypeOnInterface(this.getClass(), ApplicationListenerAdapter.class.getName());
        } catch (IllegalArgumentException e) {
            return ApplicationEvent.class;
        }
    }

}
