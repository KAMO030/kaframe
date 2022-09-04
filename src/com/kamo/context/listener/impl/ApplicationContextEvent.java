package com.kamo.context.listener.impl;

import com.kamo.context.ApplicationContext;
import com.kamo.context.listener.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {
    public ApplicationContextEvent(Object source) {
        super(source);
    }
    public ApplicationContext getApplicationContext(){
        return (ApplicationContext) this.getSource();
    }
}
