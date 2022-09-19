package com.kamo.context.factory;

public interface ClassLoadAware  extends Aware {
    void setClassLoad(ClassLoader classLoader);
    @Override
    default Class[] getAwareTypes() {
        return new Class[] {ClassLoader.class};
    }

    @Override
   default void setAware(Object... awareValue){
        setClassLoad((ClassLoader) awareValue[0]);
    };
}
