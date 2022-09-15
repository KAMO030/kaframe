package com.kamo.context.listener.impl;

import java.lang.reflect.Method;

public class ContextRefreshedListenerMethodAdapter extends ApplicationListenerMethodAdapter<ContextRefreshedEvent> {
    public ContextRefreshedListenerMethodAdapter(Object source, Method method) {
        super(source, method,null);
    }

    @Override
    public Class supportsEventType() {
        return ContextRefreshedEvent.class;
    }
}
