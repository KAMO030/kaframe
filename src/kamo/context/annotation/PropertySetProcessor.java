package kamo.context.annotation;

import kamo.context.*;
import kamo.context.factory.BeanInstanceProcessor;

import java.lang.reflect.Field;

public class PropertySetProcessor implements BeanInstanceProcessor {
    private BeanFactory factory;

    public PropertySetProcessor(BeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object instanceBefore(String beanName, BeanDefinition beanDefinition) {
        return null;
    }

    @Override
    public void instanceAfter(String beanName, BeanDefinition beanDefinition, Object bean) {
        Property[] propertys = beanDefinition.getPropertys();
        Class<?> beanClass = bean.getClass();
        for (Property property : propertys ) {
            String name = property.getName();
            Field field;
            try {
                field = property.getField();
                field.setAccessible(true);
                if (field.get(bean)==null) {
                    Object value = getPropertyValue(property);
                    field.set(bean,value );
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isNeedProxy(Class type) {
        return type.isInterface();
    }
//property.isLazed()
//        ? LazedProxy.getLazedProxy(beanClass,
//            () -> getPropertyValue(property))
//            :getPropertyValue(property);
    protected Object getPropertyValue(Property property) {
        Object value = property.getValue();
        //如果之前没有设置值
        if (value==null) {
            //如果该属性是懒加载的
            if (property.isLazed()) {
                value = getProxyPropertyValue(property);
            }else if(!factory.isInUse(property.getType())){
                //如果需要的此类型没有正在创建，没有出现了循环依赖
                value =  factory.getBean(property.getName(), property.getType());
            }else if (isNeedProxy(property.getType())){

                value = getProxyPropertyValue(property);
            } else {

                value = factory.getInUseAndRemove(property.getName(), property.getType());
            }
        }
        else if (!property.getType().equals(property.getValueType())) {

        }
        property.setValue(value);
        return value;
    }
   private Object getProxyPropertyValue(Property property){
       return LazedProxy.getLazedProxy(property.getType(), () -> factory.getBean(property.getName(), property.getType()));
    }
}
