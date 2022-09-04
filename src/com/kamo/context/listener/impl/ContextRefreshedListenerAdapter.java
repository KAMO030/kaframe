package com.kamo.context.listener.impl;

import java.lang.reflect.Method;

public class ContextRefreshedListenerAdapter extends ApplicationListenerAdapter<ContextRefreshedEvent> {
    public ContextRefreshedListenerAdapter(Object source, Method method) {
        super(source, method,null);
    }

    @Override
    public Class getEventType() {
        return ContextRefreshedEvent.class;
    }
}
