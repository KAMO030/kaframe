package com.kamo.context.annotation;

public  interface Scanner {
    void scan(String[] basePackages);
    void register(Class beanClass);
    boolean isRegisterClass(Class loaderClass);
}
