package com.kamo.context.condition;

import java.lang.reflect.AnnotatedElement;

public interface ConditionMatcher {
    boolean isMeeConditions(Object obj);
}
