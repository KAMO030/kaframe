package com.kamo.proxy;

import java.lang.reflect.Method;
@FunctionalInterface
public interface PointParser {
    Advisor getAdvisor(Method method , Object aspectObj);
}
