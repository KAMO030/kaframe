package kamo.proxy;

public interface TargetSource<T> {
    T getTarget();
    boolean isStatic();
    Class<T> getTargetClass();
}
