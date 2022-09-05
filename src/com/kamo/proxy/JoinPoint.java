package com.kamo.proxy;

import com.sun.istack.internal.Nullable;

public interface JoinPoint {
    @Nullable
    Object proceed() throws Throwable;
    @Nullable
    Object getThis();
}
