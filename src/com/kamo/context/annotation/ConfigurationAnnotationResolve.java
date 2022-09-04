package com.kamo.context.annotation;

import com.kamo.context.*;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.factory.BeanFactoryPostProcessor;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.factory.BeanPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.util.AnnotationMetadata;
import com.kamo.util.Converter;
import com.kamo.util.AnnotationUtils;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class ConfigurationAnnotationResolve implements Resolver {
    private ConfigurableListableBeanFactory configRegistry;

    private Class configClass;

    private ConditionMatcher conditionMatcher;

    public ConfigurationAnnotationResolve(ConfigurableListableBeanFactory configRegistry, ConditionMatcher conditionMatcher,  Class configClass) {
        this.configRegistry = configRegistry;
        this.configClass = configClass;
        this.conditionMatcher = conditionMatcher;
    }

    @Override
    public void parse() {
        List<AnnotationMetadata<Import>> metadatas = AnnotationUtils.getAnnotationMetadatas(configClass, Import.class);
        for (AnnotationMetadata<Import> metadata : metadatas) {
            try {
                importConfiguration(metadata);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void importConfiguration(AnnotationMetadata<Import> metadata) throws NoSuchFieldException {
        Class[] value = metadata.getAnnotation().value();

        for (Class configClass : value) {
            if (AnnotationUtils.isAnnotationPresent(configClass,Configuration.class)) {
                AnnotationConfigUtils.parseConfiguration(configRegistry, (BeanDefinitionRegistry) configRegistry,conditionMatcher,configClass);
                continue;
            }
            if (isPlugin(configClass)) {
                String beanName = Introspector.decapitalize(configClass.getSimpleName());
                BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(configClass);
                if (BeanDefinitionImportRegistry.class.isAssignableFrom(configClass)) {
                    Field annotationMetadata = BeanDefinitionImportRegistry.class.getDeclaredField("annotationMetadata");
                    Property property = new Property(annotationMetadata);
                    property.setValue(metadata);
                    property.setType(annotationMetadata.getType());
                    property.setName("annotationMetadata");

                    beanDefinition.addProperty(property);
                }
                configRegistry.registerConfiguration(beanName, beanDefinition);
            }
        }
    }

    protected boolean isPlugin(Class configClass) {
        return BeanFactoryPostProcessor.class.isAssignableFrom(configClass)
                || BeanInstanceProcessor.class.isAssignableFrom(configClass)
                || BeanPostProcessor.class.isAssignableFrom(configClass)
                || Converter.class.isAssignableFrom(configClass);
    }


}
