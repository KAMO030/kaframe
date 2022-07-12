package kamo.factory;

import java.lang.reflect.InvocationHandler;

public abstract class PointCut implements InvocationHandler {
    protected AspectJ aspectJ;
    protected Object target;
    public void setAspectJ(AspectJ aspectJ) {
        this.aspectJ = aspectJ;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
