package com.kamo.proxy.impl;

import com.kamo.proxy.Advice;
import com.kamo.proxy.Advisor;
import com.kamo.proxy.Pointcut;
import com.kamo.proxy.annotation.After;
import com.kamo.proxy.PointParser;

import java.lang.reflect.Method;

public class AfterPoint implements PointParser {
    @Override
    public Advisor getAdvisor(Method method, final Object aspectObj) {
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
