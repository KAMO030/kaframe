package com.kamo.context.annotation;

import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanDefinitionBuilder;
import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.ClassLoadAware;
import com.kamo.util.AnnotationMetadata;
import com.kamo.util.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertySourceConfig extends BeanDefinitionImportRegistry implements ClassLoadAware {

    private Map<String, Properties> propertyMap ;
    private ClassLoader classLoader;

    @Override
    public void postProcessBeanDefinitionRegistry(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) throws BeansException {
        if (propertyMap == null) {
            PropertySource propertySource = (PropertySource) annotationMetadata.getAnnotationInSrc(PropertySource.class);
            init(propertySource.value());
        }
        for (Map.Entry<String, Properties> propertiesEntry : propertyMap.entrySet()) {
            Properties properties = propertiesEntry.getValue();
            String path = propertiesEntry.getKey();
            regisBean(path,properties,registry);
        }
    }

    private void init(String[]paths) {
        propertyMap = new ConcurrentHashMap<>();
        for (String path : paths) {
            Properties properties = new Properties();
            try {
                properties.load(classLoader.getResourceAsStream(path));
            } catch (IOException e) {
                throw new RuntimeException("找不到路径名为: " + path + " 的文件");
            }
            propertyMap.put(path.substring(0, path.lastIndexOf('.')), properties);
        }
    }



    private void regisBean(String path, Properties properties,BeanDefinitionRegistry registry) {
        for (String keyName : properties.stringPropertyNames()) {
            BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(String.class,()->properties.getProperty(keyName));
            registry.registerBeanDefinition(keyName, beanDefinition);
        }
        BeanDefinition propertyDefinition = BeanDefinitionBuilder.getBeanDefinition(Properties.class,()->properties);
        registry.registerBeanDefinition(path, propertyDefinition);
    }

    @Override
    public void setClassLoad(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


}
