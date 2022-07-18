package com.kamo.proxy.impl;

import com.kamo.proxy.Advice;

public class AfterAdvisor implements Advice {
    @Override
    public Object invoke(JdkMethodInvocation invocation) throws Throwable {
       Object result =  invocation.proceed();
        return null;
    }
}
