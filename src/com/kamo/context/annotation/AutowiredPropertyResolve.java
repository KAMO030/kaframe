package com.kamo.context.annotation;


import com.kamo.context.BeanDefinition;
import com.kamo.context.Property;
import com.kamo.util.ReflectUtils;

import java.lang.reflect.Field;


public class AutowiredPropertyResolve extends AbstractPropertyResolve {


    private BeanDefinition beanDefinitio;

    public AutowiredPropertyResolve(BeanDefinition beanDefinition) {
        this.beanDefinitio = beanDefinition;

    }

    @Override
    public void parse() {
        Class beanClass = beanDefinitio.getBeanClass();
        do {
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (needParse(field)) {
                    doParse(field);
                }
            }
        } while (beanClass != null
                &&! beanClass.isInterface()
                && !(beanClass = beanClass.getSuperclass()).equals(Object.class));
    }

    protected void doParse(Field field) {
        Property property = new Property(field);
        String value = ReflectUtils.getAnnotation(field, Autowired.class).value();
        if (!value.equals("")) {
            property.setValue(value);
        }
        beanDefinitio.addProperty(property);
    }


}
