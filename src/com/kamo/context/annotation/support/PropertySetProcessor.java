package com.kamo.context.annotation.support;

import com.kamo.bean.BeanDefinition;
import com.kamo.bean.annotation.Order;
import com.kamo.bean.support.Property;
import com.kamo.context.converter.ConverterRegistry;
import com.kamo.context.factory.*;
import com.kamo.core.support.impl.LazedInvocationHandler;
import com.kamo.core.util.ProxyUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Order(2)
public class PropertySetProcessor implements BeanInstanceProcessor , ApplicationContextAware {
    private BeanFactory factory;



    @Override
    public Object instanceBefore(String beanName, BeanDefinition beanDefinition) {
        return null;
    }

    @Override
    public void instanceAfter(String beanName, BeanDefinition beanDefinition, Object bean) {
        Property[] propertys = beanDefinition.getPropertys();
        Class<?> beanClass = bean.getClass();
        for (Property property : propertys) {
            property.setBean(bean);
            if (property.isNeedAssign()) {
                Object value = getPropertyValue(property);
                property.assignPro(value);
            }
        }
    }

    protected Object getPropertyValue(Property property) {
        Object value = property.getValue();
        Class filedType = property.getType();
        //如果之前没有设置值
        if (value == null) {
            //如果该属性是懒加载的
            value = property.isLazed()?
                 getProxyPropertyValue(property):
                    getValue(property);
        }else if(!filedType.isAssignableFrom(property.getValueType())) {
            value =  ConverterRegistry.convert( value,filedType);
        }
        property.setValue(value);
        return value;
    }
    protected <T> Object getValue(Property property) {
        Object value = property.getValue();
        Class<T> requiredType = property.getType();
        String name = property.getName();
        if (requiredType.isArray()) {
            return factory.getBeans(requiredType.getComponentType()).toArray((T[]) Array.newInstance(requiredType.getComponentType(),0));
        }else if (List.class.isAssignableFrom(requiredType)){
           ParameterizedType parameterizedType = (ParameterizedType) property.getGenericType();
            return factory.getBeans((Class)parameterizedType.getActualTypeArguments()[0]);
        }
        return factory.getBean(name,requiredType);
    }

    private Object getProxyPropertyValue(Property property) {
        InvocationHandler lazedInvocationHandler = new LazedInvocationHandler( () -> getValue(property));
        return ProxyUtils.creatProxyInstance(property.getType(),lazedInvocationHandler);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.factory = applicationContext;
    }
}
