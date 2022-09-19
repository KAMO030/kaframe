package com.kamo.context.listener;

import com.kamo.context.factory.Aware;
import com.kamo.context.listener.impl.DefaultEventMulticaster;

public interface ApplicationEventMulticasterAware  extends Aware {
    void setApplicationEventMulticaster(ApplicationEventMulticaster applicationEventMulticaster);
    @Override
    default Class[] getAwareTypes() {
        return new Class[] {ApplicationEventMulticaster.class};
    }
    @Override
    default void setAware(Object... awareValue) {
        setApplicationEventMulticaster((ApplicationEventMulticaster) awareValue[0]);
    }

    @Override
    default String[] getAwareNames() {
        return new String[] {DefaultEventMulticaster.DEFAULT_EVENT_MULTICASTER_NAME};
    }
}
