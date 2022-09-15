package com.kamo.context.listener.impl;

import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.ApplicationEventMulticaster;
import com.kamo.context.listener.ApplicationListener;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultEventMulticaster implements ApplicationEventMulticaster {
    public static final String DEFAULT_EVENT_MULTICASTER_NAME = Introspector.decapitalize(DefaultEventMulticaster.class.getSimpleName());
    private final List<ApplicationListener<?>> listeners = new ArrayList<>();
    private Executor executor = new ThreadPoolExecutor(5,10,10,
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(10),new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        this.getApplicationListeners(event).forEach(listener -> {
            executor.execute(()->listener.onApplicationEvent(event));
        });
    }

    private List<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        Class eventType = event.getClass();
        List<ApplicationListener> matchListeners = new ArrayList<>();
        listeners.forEach(listener -> {
            if (listener.supportsEventType().equals(eventType)) {
                matchListeners.add(listener);
            }
        });
        return matchListeners;
    }

}
