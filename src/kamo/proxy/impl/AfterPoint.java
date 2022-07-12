package kamo.proxy.impl;

import kamo.proxy.A;
import kamo.proxy.Advice;
import kamo.proxy.Advisor;
import kamo.proxy.Pointcut;
import kamo.proxy.annotation.After;

import java.lang.reflect.Method;

public class AfterPoint implements A {
    @Override
    public Advisor getAdvisor(Method method,final Object aspectObj) {
        String pointPath = method.getAnnotation(After.class).value();
        String methodName = pointPath.substring(pointPath.lastIndexOf('.') + 1, pointPath.lastIndexOf('('));
        String classpath = pointPath.substring(0, pointPath.lastIndexOf('.'));
        String finalMethodName = methodName;

        Advisor advisor = new Advisor() {
            @Override
            public boolean classFilter(Class<?> targetClass) {
                return getPointcut().classFilter(targetClass);
            }

            @Override
            public Advice getAdvice() {
                return invocation -> {
                        Object result = invocation.proceed();
                        method.invoke(aspectObj);
                        return result;
                    };
            }

            @Override
            public Pointcut getPointcut() {
                return new Pointcut() {
                    @Override
                    public boolean classFilter(Class<?> targetClass) {
                        return targetClass.getName().equals(classpath);
                    }

                    @Override
                    public boolean matches(Method method, Class<?> targetClass) {
                        return method.getName().equals(finalMethodName);
                    }
                };
            }
        };
        return advisor;
    }
}
