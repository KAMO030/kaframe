package com.kamo.context.annotation;

import com.kamo.context.*;
import com.kamo.context.factory.ApplicationContextAware;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.converter.ConverterRegistry;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Order(1)
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
        return LazedProxy.getLazedProxy(property.getType(), () -> getValue(property));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.factory = applicationContext;
    }
}
