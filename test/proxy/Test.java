package proxy;

import kamo.proxy.*;
import kamo.proxy.annotation.After;
import kamo.proxy.annotation.Before;

import java.lang.reflect.Method;

public class Test {
  private   Class aClass;

    public Test(Class aClass) {
        this.aClass = aClass;
        try {
            init();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws InstantiationException, IllegalAccessException {
        Object o = aClass.newInstance();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            String path = null;
            String methodName = null;
            if (declaredMethod.isAnnotationPresent(After.class)) {
                String value = declaredMethod.getAnnotation(After.class).value();
                methodName = value.substring(value.lastIndexOf('.')+1, value.lastIndexOf('('));
                path = value.substring(0,value.lastIndexOf('.'));
            }else if (declaredMethod.isAnnotationPresent(Before.class)) {
                String value = declaredMethod.getAnnotation(Before.class).value();
                methodName = value.substring(value.lastIndexOf('.')+1, value.lastIndexOf('('));
                path = value.substring(0,value.lastIndexOf('.'));
            }
            String finalPath = path;
            String finalMethodName = methodName;
            Advisor advisor = new Advisor() {
                @Override
                public boolean classFilter(Class<?> targetClass) {
                    return targetClass.getName().equals(finalPath);
                }

                @Override
                public Advice getAdvice() {
                    if (declaredMethod.isAnnotationPresent(After.class)){
                        return invocation -> {
                            Object result =  invocation.proceed();
                            declaredMethod.invoke(o);
                            return result;
                        };
                    }else if (declaredMethod.isAnnotationPresent(Before.class)) {
                        return  invocation -> {
                            declaredMethod.invoke(o);
                            return invocation.proceed();
                        };
                    }
                    return null;
                }

                @Override
                public Pointcut getPointcut() {
                    return new Pointcut() {
                        @Override
                        public boolean classFilter(Class<?> targetClass) {
                            return targetClass.getName().equals(finalPath);
                        }

                        @Override
                        public boolean matches(Method method, Class<?> targetClass) {
                            return method.getName().equals(finalMethodName);
                        }
                    };
                }
            };
            AdvisorRegister.registerAdvisor(advisor);
        }
    }
}
