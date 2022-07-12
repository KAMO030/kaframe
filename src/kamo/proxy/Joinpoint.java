package kamo.proxy;

import com.sun.istack.internal.Nullable;

public interface Joinpoint {
    @Nullable
    Object proceed() throws Throwable;
    @Nullable
    Object getThis();
}
