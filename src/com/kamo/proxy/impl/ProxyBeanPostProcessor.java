package com.kamo.proxy.impl;

import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanPostProcessor;
import com.kamo.proxy.AdvisorRegister;

public class ProxyBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.getInterfaces().length == 0) {return bean;}
        if (AdvisorRegister.classFilter(beanClass)) {
            return new JdkProxyFactory(bean).getProxy();
        }
        return bean;
    }
}
