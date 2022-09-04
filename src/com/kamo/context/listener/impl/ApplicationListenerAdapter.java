package com.kamo.context.listener.impl;

import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.ApplicationListener;
import com.kamo.util.ReflectUtil;
import com.kamo.util.exception.ReflectException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ApplicationListenerAdapter<E extends ApplicationEvent> implements ApplicationListener<E> {
    private Object source;
    private Method method;
    private String[] multicasterNames;
    private Class<E> eventType;

    public ApplicationListenerAdapter(Object source, Method method, String[] multicasterNames) {
        this.source = source;
        this.method = method;
        this.multicasterNames = multicasterNames;
    }

    public ApplicationListenerAdapter(Object source, Method method, String[] multicasterNames, Class<E>eventType) {
        this(source,method,multicasterNames);
        this.eventType = eventType;
    }

    @Override
    public String[] getMulticasterNames() {
        return multicasterNames==null || multicasterNames.length==0 ?
                ApplicationListener.super.getMulticasterNames() : multicasterNames;
    }

    @Override
    public void onApplicationEvent(E event) {
        Object[] args = getArgsFromEvent(event);
        try {
            ReflectUtil.invokeMethod(method,source,args);
        } catch (ReflectException e) {
            e.printStackTrace();
        }
    }

    protected Object[] getArgsFromEvent(E event) {
        return new Object[]{event};
    }

    @Override
    public Class getEventType() {
        if (!eventType.equals(ApplicationEvent.class)) {
            return eventType;
        }
        try {
            return ReflectUtil.getActualTypeOnInterface(this.getClass(),ApplicationListenerAdapter.class.getName());
        }catch (IllegalArgumentException e) {
            return ApplicationEvent.class;
        }
    }
}
