package com.kamo.cloud;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(final Class interfaceClass,final String version) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Invocation invocation = new Invocation(interfaceClass.getName(),
                        version,
                        method.getName(),
                        method.getParameterTypes(),
                        args );

                Invoker invoker = ClusterInvoker.join(interfaceClass.getName(),version);

                return invoker.invoke(invocation);

            }
        });
    }

}
