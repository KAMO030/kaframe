package com.kamo.core.cglib;

public abstract class ProxyClassWriter {
    protected Class superclass;
    protected String superName;
    protected String proxyClassName;

    public ProxyClassWriter(Class superClass) {
        this.superclass = superClass;
    }

    public abstract byte[] getProxyClassByteArray() ;
}
