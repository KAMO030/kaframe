package com.kamo.context;


import com.kamo.bean.BeanDefinition;
import com.kamo.context.factory.*;
import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.ApplicationEventMulticaster;
import com.kamo.context.listener.impl.ContextRefreshedEvent;
import com.kamo.context.listener.impl.DefaultEventMulticaster;
import com.kamo.core.io.impl.ResourceLoaderManager;
import com.kamo.core.util.ClassUtils;

import java.util.List;

public class GenericApplicationContext extends AbstractConfigurableListableBeanFactory implements ApplicationContext {

    protected ApplicationEventMulticaster eventMulticaster;

    public GenericApplicationContext() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public GenericApplicationContext(ClassLoader classLoader) {
        Thread.currentThread().setContextClassLoader(classLoader);
        initialize();
    }

    protected void initialize() {

        eventMulticaster = new DefaultEventMulticaster();

        addSingletonBeans(this, eventMulticaster, ClassUtils.getDefaultClassLoader(),new ResourceLoaderManager());
    }

    public void refresh() {

        //初始化注册BeanFactoryPostProcessor
        registerBeanFactoryPostProcessorBeanDefinitions();

        preInstantiateFactoryPostProcessors();
        //执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors();

        preInstantiateSingletons();
    }

    public void registerBeanFactoryPostProcessorBeanDefinitions() {
        register(FactoryLoader.load(ApplicationProcessor.class) );
    }





    private void preInstantiateFactoryPostProcessors() {
        String[] beanDefinitionNames = this.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
            registerProcessor(beanName, beanDefinition);
        }
    }


    private void preInstantiateSingletons() {
        String[] beanDefinitionNames = this.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
            if (!beanDefinition.isSingleton() || beanDefinition.isLazyInit()) {
                continue;
            }
            this.getSingletonBean(beanName, beanDefinition);
        }
        this.publishEvent(new ContextRefreshedEvent(this));
    }


    public void register(Class... componentClasses) {
        for (Class componentClass : componentClasses) {
            this.registerBeanDefinition(componentClass);
        }
    }



    @Override
    public void publishEvent(ApplicationEvent event) {
        eventMulticaster.multicastEvent(event);
    }


}
