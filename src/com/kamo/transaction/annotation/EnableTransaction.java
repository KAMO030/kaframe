package com.kamo.transaction.annotation;

import com.kamo.bean.annotation.Import;
import com.kamo.proxy.impl.ProxyBeanPostProcessor;
import com.kamo.transaction.TransactionManagerRegistryPostProcessor;
import com.kamo.transaction.support.TransactionManagerBeanPostProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({TransactionManagerBeanPostProcessor.class, TransactionManagerRegistryPostProcessor.class,ProxyBeanPostProcessor.class})
public @interface EnableTransaction {
    boolean autoRegistryManager() default true;
}
