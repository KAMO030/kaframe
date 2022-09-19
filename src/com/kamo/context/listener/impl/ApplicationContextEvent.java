package com.kamo.context.listener.impl;

import com.kamo.context.factory.ApplicationContext;
import com.kamo.context.listener.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent<ApplicationContext> {
    private ApplicationContext applicationContext;
    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
        applicationContext = source;
    }
    public ApplicationContext getApplicationContext(){
        return (ApplicationContext) this.getSource();
    }
}
