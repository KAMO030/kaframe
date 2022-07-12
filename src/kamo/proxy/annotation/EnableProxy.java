package kamo.proxy.annotation;

import kamo.context.annotation.Import;
import kamo.proxy.impl.ProxyConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ProxyConfig.class)
public @interface EnableProxy {
    String[] path()default {""};
}
