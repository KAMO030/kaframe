package com.kamo.proxy;

public interface  Advisor  {
    boolean classFilter(Class<?> targetClass);
    Advice getAdvice();
    Pointcut getPointcut();


}
