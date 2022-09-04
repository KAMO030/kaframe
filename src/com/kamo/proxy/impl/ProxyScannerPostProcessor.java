package com.kamo.proxy.impl;

import com.kamo.context.BeanDefinitionRegistry;
import com.kamo.context.annotation.BeanDefinitionImportRegistry;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.ClassLoadAware;
import com.kamo.util.AnnotationMetadata;

public class ProxyScannerPostProcessor extends BeanDefinitionImportRegistry  {

    private AspectScanner scanner = new AspectScanner();



    @Override
    public void postProcessBeanDefinitionRegistry(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) throws BeansException {
        String[] packages = (String[]) annotationMetadata.getSrcAnnotationValue("path");
        if(packages.length==0){
            String className = annotationMetadata.getSrcAnnotationElementClassName();
            int endLength = className.lastIndexOf('.');
            packages = new String[]{ className.substring(0,endLength!=-1?endLength:className.length())};
        }
        scanner.scan(packages);
    }
}
