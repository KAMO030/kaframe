package kamo.context_rpc;

import kamo.cloud.RpcProxyFactory;
import kamo.context.BeanDefinition;
import kamo.context.Property;
import kamo.context.annotation.Reference;
import kamo.context.factory.BeanInstanceProcessor;

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
