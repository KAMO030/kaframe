package com.kamo.proxy;

import com.kamo.proxy.impl.JdkMethodInvocation;

public interface Advice {
    Object invoke(JdkMethodInvocation invocation) throws Throwable;
}
