package com.kamo.context.annotation.support;

import com.kamo.bean.annotation.Bean;
import com.kamo.bean.annotation.ComponentScan;
import com.kamo.bean.support.AnnotationBeanDefinitionBuilder;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.Resolver;
import com.kamo.core.util.AnnotationUtils;

import java.lang.reflect.Method;

public class ConfigurationClassResolve implements Resolver {

    private ApplicationContext context;
    private Class configBeanClass;
    private ConditionMatcher methodMatcher;

    public ConfigurationClassResolve(ApplicationContext context, ConditionMatcher methodMatcher, Class configBeanClass) {
        this.methodMatcher = methodMatcher;
        this.configBeanClass = configBeanClass;
        this.context = context;
    }
    @Override
    public void parse(){
        if (needScan()) {
            scan();
        }
        if (needLoadMethods()) {
            loadMethods();
        }
    }

    private void scan() {
        String[] basePackages = AnnotationUtils.getAnnotation(configBeanClass, ComponentScan.class).value();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(methodMatcher,context);

        if (basePackages.length!=0) {
            scanner.scan(basePackages);
            return;
        }
        String basePackage = configBeanClass.getName();
        int endIndex = basePackage.lastIndexOf('.');

        basePackage = endIndex != -1 ?basePackage.substring(0, endIndex) : "";
        scanner.scan(new String[]{basePackage});
    }

    protected boolean needScan() {
        return AnnotationUtils.isAnnotationPresent(configBeanClass,ComponentScan.class);
    }

    protected void loadMethods() {
        Method[] methods = configBeanClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)&&methodMatcher.isMeeConditions(method)) {
                String name = method.getAnnotation(Bean.class).name();
                name = name.equals("") ? method.getName() : name;
                BeanDefinition methodBeanDefinition = AnnotationBeanDefinitionBuilder.getBeanDefinition(method,()-> context.getBean(configBeanClass));
                context.registerBeanDefinition(name,methodBeanDefinition);
            }
        }
    }

    protected boolean needLoadMethods() {
        boolean flag = true;
        return flag;
    }
}
