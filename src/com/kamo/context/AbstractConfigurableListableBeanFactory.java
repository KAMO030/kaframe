package com.kamo.context;

import com.kamo.bean.BeanDefinition;
import com.kamo.bean.annotation.Order;
import com.kamo.context.converter.Converter;
import com.kamo.context.converter.ConverterRegistry;
import com.kamo.context.factory.*;
import com.kamo.core.util.AnnotationUtils;
import com.kamo.core.util.ListUtils;

import java.beans.Introspector;
import java.util.*;

public class AbstractConfigurableListableBeanFactory extends AbstractBeanFactory implements ConfigurableListableBeanFactory {
    protected final List<BeanFactoryPostProcessor> beanFactoryPostProcessors;
    protected List<BeanInstanceProcessor> beanInstanceProcessors;
    protected List<BeanPostProcessor> beanPostProcessors;



    public AbstractConfigurableListableBeanFactory() {
        this.beanFactoryPostProcessors = new ArrayList<>();
        this.beanInstanceProcessors = new ArrayList<>();
        this.beanPostProcessors = new ArrayList<>();

    }

    @Override
    protected Object applyBeanInstanceBeforeProcessor(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        for (BeanInstanceProcessor instanceProcessor : beanInstanceProcessors) {
            bean = instanceProcessor.instanceBefore(beanName, beanDefinition);
            if (Objects.nonNull(bean)) {
                break;
            }
        }
        return bean;
    }

    @Override
    protected void applyBeanInstanceAfterProcessor(String beanName, BeanDefinition beanDefinition,Object bean) {
        for (BeanInstanceProcessor beanInstanceProcessor : beanInstanceProcessors) {
            beanInstanceProcessor.instanceAfter(beanName, beanDefinition, bean);
        }
    }

    @Override
    protected Object applyBeanPostProcessorBefore(Object bean, String beanName) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
        }
        return bean;
    }
    @Override
    protected Object applyBeanPostProcessorAfter(Object bean, String beanName) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
        }
        return bean;
    }

    protected void invokeBeanFactoryPostProcessors() {
        ApplicationProcessorComparable applicationProcessorComparable = new ApplicationProcessorComparable();
        int size = beanFactoryPostProcessors.size();
        beanFactoryPostProcessors.sort(applicationProcessorComparable);
        for (int i = 0; i < size; i++) {
            if (beanFactoryPostProcessors.get(i) instanceof BeanDefinitionRegistryPostProcessor) {
                ((BeanDefinitionRegistryPostProcessor) beanFactoryPostProcessors.get(i))
                        .postProcessBeanDefinitionRegistry(this);
            }
        }
        for (int i = 0; i < beanFactoryPostProcessors.size(); i++) {
            beanFactoryPostProcessors.get(i).postProcessBeanFactory(this);
        }
        for (int i = size; i < beanFactoryPostProcessors.size(); i++) {
            if (beanFactoryPostProcessors.get(i) instanceof BeanDefinitionRegistryPostProcessor) {
                ((BeanDefinitionRegistryPostProcessor) beanFactoryPostProcessors.get(i))
                        .postProcessBeanDefinitionRegistry(this);
            }
        }
        //去重
        this.beanInstanceProcessors = ListUtils.deduplication(this.beanInstanceProcessors);
        this.beanPostProcessors = ListUtils.deduplication(this.beanPostProcessors);
        //重排序
        beanInstanceProcessors.sort(applicationProcessorComparable);
        beanPostProcessors.sort(applicationProcessorComparable);
    }

    @Override
    public void registerProcessor(String beanName, BeanDefinition beanDefinition) {
        if (!this.containsBeanDefinition(beanName)) {
            this.registerBeanDefinition(beanName, beanDefinition);
        }
        if (this.containSingletonBean(beanName)) {
            return;
        }
        Class beanClass = beanDefinition.getBeanClass();
        if (BeanFactoryPostProcessor.class.isAssignableFrom(beanClass)) {
            beanFactoryPostProcessors.add(this.getBean(beanName));
        }
        if (BeanInstanceProcessor.class.isAssignableFrom(beanClass)) {
            beanInstanceProcessors.add( this.getBean(beanName));
        }
        if (BeanPostProcessor.class.isAssignableFrom(beanClass)) {
            beanPostProcessors.add(this.getBean(beanName));
        }
        if (Converter.class.isAssignableFrom(beanClass)) {
            ConverterRegistry.registerConverter(this.getBean(beanName));
        }
    }

    @Override
    public BeanDefinition registerProcessor(Class processorClass) {
        String processorName = Introspector.decapitalize(processorClass.getSimpleName());
        BeanDefinition beanDefinition ;
        if (!this.containsBeanDefinition(processorName)) {
            beanDefinition = this.registerBeanDefinition(processorName, processorClass);
        }else {
            beanDefinition =  this.getBeanDefinition(processorName);
        }
        this.registerProcessor(processorName, beanDefinition);
        return beanDefinition;
    }









    private class ApplicationProcessorComparable implements Comparator<ApplicationProcessor> {


        @Override
        public int compare(ApplicationProcessor o1, ApplicationProcessor o2) {
            Class o1Class = o1.getClass();
            Class o2Class = o2.getClass();
            Integer o1Int = AnnotationUtils.getValue(o1Class, Order.class,Integer.MAX_VALUE-1);
            Integer o2Int = AnnotationUtils.getValue(o2Class, Order.class,Integer.MAX_VALUE-1);

            return o1Int-o2Int;
        }
    }
}
