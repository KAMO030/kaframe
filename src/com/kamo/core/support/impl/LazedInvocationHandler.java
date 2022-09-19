package com.kamo.core.support.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public  class LazedInvocationHandler implements InvocationHandler {
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