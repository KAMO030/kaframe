package kamo.proxy;

import java.lang.reflect.Method;
@FunctionalInterface
public interface A {
    Advisor getAdvisor(Method method , Object aspectObj);
}
