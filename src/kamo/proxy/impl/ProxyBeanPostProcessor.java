package kamo.proxy.impl;

import kamo.context.exception.BeansException;
import kamo.context.factory.BeanPostProcessor;
import kamo.proxy.AdvisorRegister;

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
