package kamo.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AspInvocation implements InvocationHandler {
    private Method beforeMethod;
    private Method afterMethod;
    private Object aspect;
    private Object target;
    private Method afterThrowMethod;

    public AspInvocation(Object aspect, Method beforeMethod, Method afterMethod) {
        this.aspect = aspect;
        this.beforeMethod = beforeMethod;
        this.afterMethod = afterMethod;
    }

    public AspInvocation( Object aspect, Method beforeMethod, Method afterMethod,Method afterThrowMethod) {
        this.beforeMethod = beforeMethod;
        this.afterMethod = afterMethod;
        this.aspect = aspect;
        this.afterThrowMethod = afterThrowMethod;
    }
    public Object createProxy(Object target){
        this.target = target;
        return Proxy.newProxyInstance(AspInvocation.class.getClassLoader(),target.getClass().getInterfaces(),this::invoke);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnValue = null;
        if (beforeMethod != null) {
            beforeMethod.invoke(aspect);
        }
        try {
            returnValue = method.invoke(target, args);
            if (afterMethod != null) {
                afterMethod.invoke(aspect);
            }
        } catch (Exception e) {
            if(afterThrowMethod!=null){
                afterThrowMethod.invoke(aspect);
            }
        }
        return returnValue;
    }
}
