package kamo.proxy.impl;

import kamo.proxy.Advice;

public class AfterAdvisor implements Advice {
    @Override
    public Object invoke(JdkMethodInvocation invocation) throws Throwable {
       Object result =  invocation.proceed();
        return null;
    }
}
