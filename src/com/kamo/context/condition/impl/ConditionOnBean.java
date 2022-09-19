package com.kamo.context.condition.impl;

import com.kamo.bean.annotation.Autowired;
import com.kamo.context.condition.Condition;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.core.support.AnnotationMetadata;
import com.kamo.core.util.ReflectUtils;

import java.lang.reflect.AnnotatedElement;
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
        List<Class> classList = new ArrayList<>(Arrays.asList(beanClasses));
        for (int i = 0; i < beanTypes.length; i++) {
            try {
                classList.add(ReflectUtils.loadClass(classLoader, beanTypes[i]));
            }catch (Exception e){
                if (!isMiss){
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
}
