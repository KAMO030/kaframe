package com.kamo.context.factory;

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
