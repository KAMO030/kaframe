package com.kamo.context;


import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.context.factory.*;
import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.ApplicationEventMulticaster;
import com.kamo.context.listener.impl.ContextRefreshedEvent;
import com.kamo.context.listener.impl.DefaultEventMulticaster;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AbstractApplicationContext implements ApplicationContext {

    protected BeanDefinitionRegistry registry;
    protected BeanFactory beanFactory;
    protected ConfigurableListableBeanFactory configFactory;

    protected ApplicationEventMulticaster eventMulticaster;

    public AbstractApplicationContext() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public AbstractApplicationContext(ClassLoader classLoader) {
        Thread.currentThread().setContextClassLoader(classLoader);
        initialize();
    }

    protected void initialize() {
        registry = new DefaultBeanDefinitionRegistry();
        beanFactory = new DefaultBeanFactory(this);
        configFactory = new DefaultConfigurableListableBeanFactory(this, this);
        eventMulticaster = new DefaultEventMulticaster();

        addSingletonBeans(this, eventMulticaster, Thread.currentThread().getContextClassLoader());
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
        register(FactoryLoader.load(ApplicationProcessor.class));
    }

    public void addSingletonBeans(Object... beans) {
        this.beanFactory.addSingletonBeans(beans);
    }

    public void addSingletonBean(String name, Object bean) {
        this.beanFactory.addSingletonBean(name, bean);
    }

    private void preInstantiateFactoryPostProcessors() {
        String[] beanDefinitionNames = this.registry.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = this.registry.getBeanDefinition(beanName);
            registerProcessor(beanName, beanDefinition);
        }
    }

    private void invokeBeanFactoryPostProcessors() {
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = this.configFactory.getBeanFactoryPostProcessors();
        int size = beanFactoryPostProcessors.size();
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

    }

    private void preInstantiateSingletons() {
        String[] beanDefinitionNames = this.registry.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = this.registry.getBeanDefinition(beanName);
            if (!beanDefinition.isSingleton() || beanDefinition.isLazyInit()) {
                continue;
            }
            this.beanFactory.getSingletonBean(beanName, beanDefinition);
        }
        this.publishEvent(new ContextRefreshedEvent(this));
    }


    public void register(Class... componentClasses) {
        for (Class componentClass : componentClasses) {
            this.registry.registerBeanDefinition(componentClass);
        }
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        this.registry.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition registerBeanDefinition(String beanName, Class beanClass) {
       return registry.registerBeanDefinition(beanName, beanClass);
    }

    @Override
    public BeanDefinition registerBeanDefinition(Class beanClass) {
        return registry.registerBeanDefinition(beanClass);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        this.registry.removeBeanDefinition(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return this.registry.getBeanDefinition(beanName);
    }

    @Override
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.configFactory.getBeanFactoryPostProcessors();
    }

    @Override
    public Set<BeanInstanceProcessor> getBeanInstanceProcessors() {
        return this.configFactory.getBeanInstanceProcessors();
    }

    @Override
    public Set<BeanPostProcessor> getBeanPostProcessors() {
        return this.configFactory.getBeanPostProcessors();
    }


    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.registry.containsBeanDefinition(beanName);
    }

    @Override
    public void registerProcessor(String beanName, BeanDefinition beanDefinition) {
        this.configFactory.registerProcessor(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition registerProcessor(Class configClass) {
        return this.configFactory.registerProcessor(configClass);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.registry.getBeanDefinitionNames();
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.registry.getBeanDefinitionCount();
    }

    @Override
    public Map<Class, Object> getFactoryBeans() {
        return this.registry.getFactoryBeans();
    }

    @Override
    public boolean isSingletonCurrentlyInitialized(String beanName) {
        return this.registry.isSingletonCurrentlyInitialized(beanName);
    }


    @Override
    public boolean containSingletonBean(String name) {
        return this.beanFactory.containSingletonBean(name);
    }

    @Override
    public boolean containsBeanDefinition(Class type) {
        return this.beanFactory.containsBeanDefinition(type);
    }


    @Override
    public String[] getAliases(String name) {
        return this.beanFactory.getAliases(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return this.beanFactory.getBean(requiredType);
    }

    @Override
    public String[] getBeanNamesByType(Class requiredType) {
        return this.beanFactory.getBeanNamesByType(requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return this.beanFactory.getBean(requiredType, args);
    }

    @Override
    public boolean isUsing(Class type) {
        return this.beanFactory.isUsing(type);
    }

    @Override
    public Object getUsingBean(String beanName, Class type) {
        return this.beanFactory.getUsingBean(beanName, type);
    }

    @Override
    public <T> T getBean(String name) {
        return this.beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return this.beanFactory.getBean(name, requiredType);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return this.beanFactory.getBean(name, args);
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanFactory.getType(name);
    }

    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit) {
        return this.beanFactory.getType(name, allowFactoryBeanInit);
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanFactory.isPrototype(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanFactory.isSingleton(name);
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) {
        return this.beanFactory.isTypeMatch(name, typeToMatch);
    }

    @Override
    public Object[] getBeans() {
        return this.beanFactory.getBeans();
    }

    @Override
    public <T> List<T> getBeans(Class<T> requiredType) {
        return beanFactory.getBeans(requiredType);
    }

    @Override
    public Object getSingletonBean(String beanName, BeanDefinition beanDefinition) {
        return this.beanFactory.getSingletonBean(beanName, beanDefinition);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        eventMulticaster.multicastEvent(event);
    }

    @Override
    public void destroy() {
        this.beanFactory.destroy();
    }
}
