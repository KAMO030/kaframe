package com.kamo.proxy.annotation;

import com.kamo.bean.annotation.Import;
import com.kamo.proxy.impl.ProxyBeanPostProcessor;
import com.kamo.proxy.impl.ProxyScannerPostProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ProxyScannerPostProcessor.class,ProxyBeanPostProcessor.class})
public @interface EnableProxy {
    String[] path()default {};
}
