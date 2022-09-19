package com.kamo.context_rpc;

import com.kamo.bean.support.Property;
import com.kamo.cloud.Protocol;
import com.kamo.cloud.ProtocolFactory;
import com.kamo.cloud.RpcProxyFactory;
import com.kamo.cloud.URL;
import com.kamo.context.exception.BeansException;
import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.factory.BeanPostProcessor;

import java.lang.reflect.Field;

public class ReferencePostProcessor implements BeanInstanceProcessor , BeanPostProcessor {

    @Override
    public Object instanceBefore(String beanName, BeanDefinition beanDefinition) {
        Property[] propertys = beanDefinition.getPropertys();
        Class beanClass = beanDefinition.getBeanClass();
        for (int i = 0; i < propertys.length; i++) {
            try {
                Field  field = beanClass.getDeclaredField(propertys[i].getName());
                if (!isReferenceProxy(field)){
                    continue;
                }
                Class type = propertys[i].getType();
                String version = field.getAnnotation(Reference.class).version();
                propertys[i].setValue(RpcProxyFactory.getProxy(type, version));
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (!beanClass.isAnnotationPresent(RpcService.class)) {
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        int port = rpcService.port();
        String hostname = rpcService.hostname();
        String protocolName = rpcService.protocolName();
        String version = rpcService.version();
        Protocol protocol = ProtocolFactory.getProtocol(protocolName);
        Class<?>[] interfaces = beanClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            URL url = new URL(protocolName, hostname, port, version, interfaces[i].getName(), beanClass.getName());
            protocol.export(url,bean);
        }
        return bean;
    }



    private boolean isReferenceProxy(Field field) {
        return field.getType().isInterface()
                && field.isAnnotationPresent(Reference.class);
    }
}
