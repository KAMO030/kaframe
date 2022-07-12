package kamo.proxy;

import java.lang.reflect.Method;

public interface Pointcut {
    boolean classFilter(Class<?> targetClass);
    boolean matches(Method method, Class<?> targetClass);
}
