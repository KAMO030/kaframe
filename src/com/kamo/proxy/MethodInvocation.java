package com.kamo.proxy;

import java.lang.reflect.Method;

public interface MethodInvocation extends JoinPoint {
    /**
     * 获得被代理的方法
     * @return 被代理的方法
     */
    Method getMethod();

    /**
     * 获得调用代理方法时所传的实参
     * @return 调用代理方法时所传的实参
     */
    Object getArguments();
}
