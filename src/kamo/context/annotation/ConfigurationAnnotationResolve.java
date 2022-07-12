package kamo.context.annotation;

import kamo.context.BeanDefinitionRegistry;
import kamo.context.Resolve;
import kamo.context.factory.BeanFactoryPostProcessor;
import kamo.context.factory.BeanInstanceProcessor;
import kamo.context.factory.BeanPostProcessor;
import kamo.context.factory.ConfigurableListableBeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationAnnotationResolve implements Resolve {
    private ConfigurableListableBeanFactory beanFactory;

    private Annotation[] annotations;

    public ConfigurationAnnotationResolve(ConfigurableListableBeanFactory beanFactory, Class configClass) {
        this.beanFactory = beanFactory;
        this.annotations = configClass.getAnnotations();
    }

    @Override
    public void parse() {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.equals(Import.class)) {
                importConfiguration(((Import) annotation).value(), null);
            } else if (annotationType.isAnnotationPresent(Import.class)) {
                importConfiguration(annotationType.getAnnotation(Import.class).value(), annotation);
            }
        }
    }

    protected void importConfiguration(Class[] configClasses, Annotation annotation) {
        for (Class configClass : configClasses) {
            if (configClass.isAnnotationPresent(Configuration.class)) {
                new ConfigurationClassResolve((BeanDefinitionRegistry) beanFactory, configClass).parse();

            }
            try {
                if (isConfiguration(configClass)) {
                    beanFactory.registerConfiguration(instantConfig(annotation, configClass));
                }else {
                    beanFactory.registerConfiguration(configClass);
                }
            } catch (InstantiationException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected boolean isConfiguration(Class configClass) {
        return BeanFactoryPostProcessor.class.isAssignableFrom(configClass)
                || BeanInstanceProcessor.class.isAssignableFrom(configClass)
                || BeanPostProcessor.class.isAssignableFrom(configClass);
    }


    private Object instantConfig(Annotation annotation, Class configClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (annotation == null) {
            return configClass.newInstance();
        }
        Class annotationType = annotation.annotationType();
        Method[] declaredMethods = annotationType.getDeclaredMethods();
        if (declaredMethods.length == 0) {
            return configClass.newInstance();
        }
        Class[] argsTypes = new Class[declaredMethods.length];
        Object[] argsValues = new Object[declaredMethods.length];
        for (int i = 0; i < argsTypes.length; i++) {
            argsTypes[i] = declaredMethods[i].getReturnType();
            argsValues[i] = declaredMethods[i].invoke(annotation);
        }
        Object configObject = null;
        try {
            configObject = configClass.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            configObject = configClass.getConstructor(argsTypes).newInstance(argsValues);
        }
        return configObject;
    }
}
