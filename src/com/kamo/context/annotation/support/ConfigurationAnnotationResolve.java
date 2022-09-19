package com.kamo.context.annotation.support;

import com.kamo.bean.annotation.Import;
import com.kamo.bean.support.BeanDefinitionBuilder;
import com.kamo.bean.support.Property;
import com.kamo.context.condition.ConditionMatcher;
import com.kamo.context.factory.ApplicationProcessor;
import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.ConfigurableListableBeanFactory;
import com.kamo.context.factory.Resolver;
import com.kamo.core.support.AnnotationMetadata;
import com.kamo.core.util.AnnotationUtils;
import com.kamo.core.util.ReflectUtils;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.List;

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
            importProcessor(metadata);
        }
    }



    protected void importProcessor(AnnotationMetadata<Import> metadata)  {
        Class[] value = metadata.getAnnotation().value();

        for (Class configClass : value) {
            if (isProcessor(configClass)) {
                String beanName = Introspector.decapitalize(configClass.getSimpleName());
                BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(configClass);
                if (BeanDefinitionImportRegistry.class.isAssignableFrom(configClass)) {
                    Field annotationMetadata = ReflectUtils.getField(BeanDefinitionImportRegistry.class,"annotationMetadata");
                    Property property = new Property(annotationMetadata);
                    property.setValue(metadata);
                    property.setType(annotationMetadata.getType());
                    property.setName("annotationMetadata");

                    beanDefinition.addProperty(property);
                }
                configRegistry.registerProcessor(beanName, beanDefinition);
            }
        }
    }



    protected boolean isProcessor(Class configClass) {
        return ApplicationProcessor.class.isAssignableFrom(configClass);
    }


}
