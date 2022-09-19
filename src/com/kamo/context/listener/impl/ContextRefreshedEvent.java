package com.kamo.context.listener.impl;

import com.kamo.context.factory.ApplicationContext;

public class ContextRefreshedEvent extends ApplicationContextEvent {
    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

}