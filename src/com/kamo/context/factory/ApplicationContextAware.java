package com.kamo.context.factory;

import com.kamo.context.ApplicationContext;
import com.kamo.context.Aware;
import com.kamo.context.listener.ApplicationEventMulticaster;

public interface ApplicationContextAware  extends Aware {
    void setApplicationContext(ApplicationContext applicationContext);
    @Override
    default Class[] getAwareTypes() {
        return new Class[] {ApplicationContext.class};
    }

    @Override
    default void setAware(Object... awareValue){
        setApplicationContext((ApplicationContext) awareValue[0]);
    };
}
