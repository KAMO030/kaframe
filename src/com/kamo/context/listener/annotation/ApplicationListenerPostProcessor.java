package com.kamo.context.listener.annotation;

import com.kamo.context.ApplicationContext;
import com.kamo.context.BeanDefinition;
import com.kamo.context.factory.ApplicationContextAware;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.ApplicationEventMulticaster;
import com.kamo.context.listener.impl.ApplicationListenerAdapter;
import com.kamo.context.listener.impl.DefaultEventMulticaster;
import com.kamo.util.AnnotationUtils;
import com.kamo.util.ReflectUtil;
import com.kamo.util.exception.ReflectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class ApplicationListenerPostProcessor implements BeanInstanceProcessor, ApplicationContextAware {
    private ApplicationContext application;

    @Override
    public void instanceAfter(String beanName, BeanDefinition beanDefinition, Object bean) {
        for (Method method : beanDefinition.getBeanClass().getMethods()) {
            registerListener(AnnotationUtils.getAnnotation(method, Listener.class), method, bean);
        }
    }

    private void registerListener(Listener annotation, Method method, Object bean) {
        if (Objects.isNull(annotation)) {
            return;
        }
        Class<? extends ApplicationListenerAdapter> listenerType = annotation.listenerType();
        String[] multicasterNames = annotation.multicasterNames();
        Class eventType = annotation.eventType();
        ApplicationListenerAdapter listener = createdListener(listenerType, eventType, multicasterNames, method, bean);
        for (String multicasterName : listener.getMulticasterNames()) {
            application.getBean(multicasterName, ApplicationEventMulticaster.class)
                    .addApplicationListener(listener);
        }

    }

    private ApplicationListenerAdapter createdListener(Class<? extends ApplicationListenerAdapter> listenerType, Class eventType, String[] multicasterNames, Method method, Object bean) {
        ApplicationListenerAdapter listener;
        Constructor<? extends ApplicationListenerAdapter> constructor;
        if (multicasterNames.length == 0) {
            multicasterNames = new String[]{DefaultEventMulticaster.DEFAULT_EVENT_MULTICASTER_NAME};
        }
        if (listenerType.equals(ApplicationListenerAdapter.class)) {
            eventType = resolveEventType(eventType,method);
            constructor = ReflectUtil.getConstructor(listenerType, Object.class, Method.class, String[].class, Class.class);
            listener = ReflectUtil.newInstance(constructor, bean, method, multicasterNames, eventType);
        } else {
            try {
                constructor = ReflectUtil.getConstructor(listenerType, Object.class, Method.class, String[].class);
                listener = ReflectUtil.newInstance(constructor, bean, multicasterNames, method);
            } catch (ReflectException e) {
                constructor = ReflectUtil.getConstructor(listenerType, Object.class, Method.class);
                listener = ReflectUtil.newInstance(constructor, bean, method);
            }
        }
        return listener;
    }

    private Class resolveEventType(Class eventType,Method method) {
        if (eventType.equals(ApplicationEvent.class)) {
            Class<?> parameterType = method.getParameterTypes()[0];
            if (ApplicationEvent.class.isAssignableFrom(parameterType)) {
                eventType = parameterType;
            }
        }
        return eventType;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.application = applicationContext;
    }
}
