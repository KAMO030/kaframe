package kamo.proxy;

import kamo.proxy.impl.JdkMethodInvocation;

public interface Advice {
    Object invoke(JdkMethodInvocation invocation) throws Throwable;
}
