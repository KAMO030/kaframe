package com.kamo.context.annotation;

import com.kamo.context.*;
import com.kamo.context.factory.BeanFactoryPostProcessor;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.factory.BeanPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.util.AnnotationMetadata;
import com.kamo.util.Converter;
import com.kamo.util.ReflectUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class ConfigurationAnnotationResolve implements Resolve {
    private ConfigurableListableBeanFactory configRegistry;
    private Set<BeanDefinition> configBeanDefinitions;
    private Annotation[] annotations;
    private Class configClass;

    public ConfigurationAnnotationResolve(ConfigurableListableBeanFactory configRegistry, Class configClass, Set<BeanDefinition> configBeanDefinitions) {
        this.configRegistry = configRegistry;
        this.configBeanDefinitions = configBeanDefinitions;
        this.annotations = configClass.getAnnotations();
        this.configClass = configClass;
    }

    @Override
    public void parse() {
        List<AnnotationMetadata<Import>> metadatas = ReflectUtils.getAnnotationMetadatas(configClass, Import.class);
        for (AnnotationMetadata<Import> metadata : metadatas) {
            try {
                importConfiguration(metadata);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
//        for (Annotation annotation : annotations) {
//            Class<? extends Annotation> annotationType = annotation.annotationType();
//            if (annotationType.equals(Import.class)) {
//                importConfiguration(((Import) annotation).value(), null);
//            } else if (annotationType.isAnnotationPresent(Import.class)) {
//                importConfiguration(annotationType.getAnnotation(Import.class).value(), annotation);
//            }
//        }
    }

    protected void importConfiguration(AnnotationMetadata<Import> metadata) throws NoSuchFieldException {
        Class[] value = metadata.getAnnotation().value();

        for (Class configClass : value) {
            if (configClass.isAnnotationPresent(Configuration.class)) {
                configBeanDefinitions.add(configRegistry.registerConfiguration(configClass));
                new ConfigurationAnnotationResolve(configRegistry, configClass, configBeanDefinitions).parse();
                continue;
            }
            if (isConfiguration(configClass)) {
                String beanName = Introspector.decapitalize(configClass.getSimpleName());
                BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(configClass);
                if (BeanDefinitionImportRegistry.class.isAssignableFrom(configClass)) {
                    Field annotationMetadata = BeanDefinitionImportRegistry.class.getDeclaredField("annotationMetadata");
                    Property property = new Property(annotationMetadata);
                    property.setValue(metadata);
                    beanDefinition.addProperty(property);
                }
                configRegistry.registerConfiguration(beanName, beanDefinition);
            }
        }
    }

    protected boolean isConfiguration(Class configClass) {
        return BeanFactoryPostProcessor.class.isAssignableFrom(configClass)
                || BeanInstanceProcessor.class.isAssignableFrom(configClass)
                || BeanPostProcessor.class.isAssignableFrom(configClass)
                || Converter.class.isAssignableFrom(configClass);
    }


    private BeanDefinition instantConfig(Annotation annotation, Class configClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(configClass);
        if (annotation == null) {
            return beanDefinition;
        }
        Class annotationType = annotation.annotationType();
        Method[] declaredMethods = annotationType.getDeclaredMethods();
        if (declaredMethods.length == 0) {
            return beanDefinition;
        }
        Class[] argsTypes = new Class[declaredMethods.length];
        Object[] argsValues = new Object[declaredMethods.length];
        for (int i = 0; i < argsTypes.length; i++) {
            argsTypes[i] = declaredMethods[i].getReturnType();
            argsValues[i] = declaredMethods[i].invoke(annotation);
            beanDefinition.addArguments("var", new Arguments("var", argsTypes[i], argsValues[i]));
        }
        return beanDefinition;
    }
}
