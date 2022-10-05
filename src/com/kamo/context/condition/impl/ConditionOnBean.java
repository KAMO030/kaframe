package com.kamo.context.condition.impl;

import com.kamo.bean.annotation.Autowired;
import com.kamo.context.condition.Condition;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.core.support.AnnotationMetadata;
import com.kamo.core.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionOnBean implements Condition {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ClassLoader classLoader;

    @Override
    public boolean matches(AnnotationMetadata metadata, AnnotatedElement element) {
        final boolean isMiss = (boolean) metadata.getSrcAnnotationValue("IS_MISS");
        final String[] beanNames = (String[]) metadata.getSrcAnnotationValue("beanNames");
        final String[] beanTypes = (String[]) metadata.getSrcAnnotationValue("beanTypes");
        final Class[] beanClasses = (Class[]) metadata.getSrcAnnotationValue("beanClasses");

        if (isDefaultMatch(beanNames, beanTypes, beanClasses)) {
            return doDefaultMatch(isMiss,element);
        }

        List<Class> classList = new ArrayList<>(Arrays.asList(beanClasses));
        for (int i = 0; i < beanTypes.length; i++) {
            try {
                classList.add(ClassUtils.loadClass(classLoader, beanTypes[i]));
            } catch (Exception e) {
                if (!isMiss) {
                    return false;
                }
            }
        }
        for (String beanName : beanNames) {
            if (isMiss == applicationContext.containsBeanDefinition(beanName)) {
                return false;
            }
        }
        for (Class beanClass : classList) {
            if (isMiss == applicationContext.containsBeanDefinition(beanClass)) {
                return false;
            }
        }
        return true;
//        for (String beanType : beanTypes) {
//            try {
//                Class type = ReflectUtil.loadClass(classLoader, beanType);
//                if (isMiss == applicationContext.containsBean(type)) {
//                    return false;
//                }
//            } catch (Exception e) {
//                if (!isMiss){
//                    return false;
//                }
//            }
//        }

//        for (String beanName : beanNames) {
//            if (!applicationContext.containsBean(beanName)) {
//                return false;
//            }
//        }
//        for (String beanType : beanTypes) {
//            try {
//                Class type = ReflectUtil.loadClass(classLoader, beanType);
//                if (!applicationContext.containsBean(type)) {
//                    return false;
//                }
//            } catch (Exception e) {
//                return false;
//            }
//        }
    }

    private boolean doDefaultMatch(boolean isMiss, AnnotatedElement element) {
        Class beanClass = null;
        if (element instanceof Class) {
            beanClass = (Class) element;
        }else if (element instanceof Method) {
            beanClass = ((Method) element).getReturnType();
        }else {
            return false;
        }
        return isMiss !=  applicationContext.containsBeanDefinition(beanClass);
    }

    private boolean isDefaultMatch(String[] beanNames, String[] beanTypes, Class[] beanClasses) {
        return beanNames.length == 0 && beanTypes.length == 0 && beanClasses.length == 0;
    }
}
