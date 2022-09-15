package com.kamo.context;

import com.kamo.context.annotation.Order;
import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.context.factory.*;
import com.kamo.util.AnnotationUtils;
import org.jetbrains.annotations.NotNull;

import java.beans.Introspector;
import java.util.*;

public class DefaultConfigurableListableBeanFactory implements ConfigurableListableBeanFactory {
    protected final List<BeanFactoryPostProcessor> beanFactoryPostProcessors;
    protected final Set<BeanInstanceProcessor> beanInstanceProcessors;
    protected final Set<BeanPostProcessor> beanPostProcessors;

    protected final BeanDefinitionRegistry registry;
    private final BeanFactory beanFactory;

    public DefaultConfigurableListableBeanFactory(BeanFactory beanFactory,BeanDefinitionRegistry registry) {
        ApplicationProcessorComparable applicationProcessorComparable = new ApplicationProcessorComparable();
        this.beanFactoryPostProcessors = new ArrayList<>();
        this.beanInstanceProcessors = new HashSet<>();
        this.beanPostProcessors = new HashSet<>();
        this.registry = registry;
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerProcessor(String beanName, BeanDefinition beanDefinition) {
        if (!registry.containsBeanDefinition(beanName)) {
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
        if (beanFactory.containSingletonBean(beanName)) {
            return;
        }
        Class beanClass = beanDefinition.getBeanClass();
        if (BeanFactoryPostProcessor.class.isAssignableFrom(beanClass)) {
            beanFactoryPostProcessors.add(beanFactory.getBean(beanName));
        }
        if (BeanInstanceProcessor.class.isAssignableFrom(beanClass)) {
            beanInstanceProcessors.add( beanFactory.getBean(beanName));
        }
        if (BeanPostProcessor.class.isAssignableFrom(beanClass)) {
            beanPostProcessors.add(beanFactory.getBean(beanName));
        }

    }

    @Override
    public BeanDefinition registerProcessor(Class processorClass) {
        String processorName = Introspector.decapitalize(processorClass.getSimpleName());
        BeanDefinition beanDefinition ;
        if (!registry.containsBeanDefinition(processorName)) {
            beanDefinition = registry.registerBeanDefinition(processorName, processorClass);
        }else {
            beanDefinition =  registry.getBeanDefinition(processorName);
        }
        this.registerProcessor(processorName, beanDefinition);
        return beanDefinition;
    }



    @Override
    public void register(Class... beanClases) {
        for (Class beanClass : beanClases) {
            registerProcessor(beanClass);
        }
    }


    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return registry.getBeanDefinition(beanName);
    }


    @Override
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }
    @Override
    public Set<BeanInstanceProcessor> getBeanInstanceProcessors() {
        return beanInstanceProcessors;
    }
    @Override
    public Set<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }



    private class ApplicationProcessorComparable implements Comparator<ApplicationProcessor> {


        @Override
        public int compare(ApplicationProcessor o1, ApplicationProcessor o2) {
            Class o1Class = o1.getClass();
            Class o2Class = o2.getClass();
            Integer o1Int = AnnotationUtils.getValue(o1Class, Order.class,Integer.MAX_VALUE-1);
            Integer o2Int = AnnotationUtils.getValue(o2Class, Order.class,Integer.MAX_VALUE-1);

            return o2Int-o1Int;
        }
    }
}
