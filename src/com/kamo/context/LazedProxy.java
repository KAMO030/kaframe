package com.kamo.context;

import com.kamo.cglib.ProxyClass;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Supplier;

public class LazedProxy {

    public static <T> T getLazedProxy(Class<T> type, Supplier supplier) {
        if (type.isInterface()) {
            return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new LazedInvocationHandler(supplier));
        }
        Class<?>[] interfaces = type.getInterfaces();

        return (T) (interfaces.length > 0 ?
                Proxy.newProxyInstance(type.getClassLoader(), interfaces, new LazedInvocationHandler(supplier)) :
                ProxyClass.newProxyInstance(type.getClassLoader(), type, new LazedInvocationHandler(supplier))

        );

    }

    private static class LazedInvocationHandler implements InvocationHandler {
        private Supplier supplier;
        private Object target;

        public LazedInvocationHandler(Supplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (target == null) {
                target = supplier.get();
            }
//           if (method.getName().equals("toString")&&args==null){
//               return target.getClass().getName()+"$proxy@"+Integer.toHexString(target.hashCode());
//           }
            try {
                return method.invoke(target, args);
            } catch (IllegalArgumentException e) {
                return target.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(target, args);
            }

        }
    }
}
