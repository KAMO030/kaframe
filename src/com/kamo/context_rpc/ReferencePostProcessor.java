package com.kamo.context_rpc;

import com.kamo.cloud.RpcProxyFactory;
import com.kamo.context.BeanDefinition;
import com.kamo.context.Property;
import com.kamo.context.annotation.Reference;
import com.kamo.context.factory.BeanInstanceProcessor;

import java.lang.reflect.Field;

public class ReferencePostProcessor implements BeanInstanceProcessor {

    @Override
    public Object instanceBefore(String beanName, BeanDefinition beanDefinition) {
        Property[] propertys = beanDefinition.getPropertys();
        for (int i = 0; i < propertys.length; i++) {
            if (!isReferenceProxy(propertys[i])) {
                continue;
            }
            Class type = propertys[i].getType();
            Field field = propertys[i].getField();
            Reference reference = field.getAnnotation(Reference.class);
            String version = reference.value();
            propertys[i].setValue(RpcProxyFactory.getProxy(type, version));
        }
        return null;
    }

    private boolean isReferenceProxy(Property property) {
        return property.getType().isInterface()
                && property.getField().isAnnotationPresent(Reference.class);
    }
}
