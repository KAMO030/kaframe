package com.kamo.context;

public final  class BeanFinder {
    private BeanFinder() {

    }
    public static Object findBean(BeanFactory factory,Class< ? > type,String beanName){
        Object bean = factory.getBean(type);
        if (bean == null) {
            bean = factory.getBean(beanName);
        }
        return bean;
    }
}
