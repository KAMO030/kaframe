package com.kamo.context;

import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.context.factory.BeanDefinitionRegistryPostProcessor;
import com.kamo.context.factory.BeanFactoryPostProcessor;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.factory.BeanPostProcessor;

import java.beans.Introspector;
import java.util.List;
import java.util.Map;
import java.util.Set;

public  class AbstractApplicationContext1 implements ApplicationContext, BeanDefinitionRegistry {
    protected DefaultBeanDefinitionRegistry registry;
    protected DefaultBeanFactory beanFactory;
    protected  DefaultConfigurableListableBeanFactory configFactory;


    public AbstractApplicationContext1() {
        registry = new DefaultBeanDefinitionRegistry();
        beanFactory = new DefaultBeanFactory(this,this);
        configFactory = new DefaultConfigurableListableBeanFactory(this,this);
    }



    public void refresh() {
        //初始化注册BeanFactoryPostProcessor
        registerBeanFactoryPostProcessors();

        preInstantiateFactoryPostProcessors();
        //执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors();

        preInstantiateSingletons();
    }

    public void registerBeanFactoryPostProcessors() {
        this.beanFactory.singletonBeans.put(Introspector.decapitalize(this.getClass().getSimpleName()), this);
        register(this.getClass());
    }

    private void preInstantiateFactoryPostProcessors() {
        this.registry.beanDefinitions.entrySet().forEach(beanDefinitionEntry -> {
            registerConfiguration(beanDefinitionEntry.getKey(),
                    beanDefinitionEntry.getValue());
        });
    }

    private void invokeBeanFactoryPostProcessors() {
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = this.configFactory.beanFactoryPostProcessors;
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
        Map<String, BeanDefinition> beanDefinitions = this.registry.beanDefinitions;
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitions.entrySet()) {
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinition.isSingleton()) {
                continue;
            }
            this.beanFactory.getSingletonBean(beanName, beanDefinition);
        }
    }



    public void register(Class... componentClasses) {

    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        this.registry.registerBeanDefinition(beanName, beanDefinition);
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
    public void registerConfiguration(String beanName, BeanDefinition beanDefinition) {
        this.configFactory.registerConfiguration(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition registerConfiguration(Class configClass) {
        return this.configFactory.registerConfiguration(configClass);
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
    public boolean isBeanNameInUse(String beanName) {
        return this.registry.isBeanNameInUse(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
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
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return this.beanFactory.getBean(requiredType, args);
    }

    @Override
    public boolean isInUse(Class type) {
        return this.beanFactory.isInUse(type);
    }

    @Override
    public Object getInUseAndRemove(String beanName, Class type) {
        return this.beanFactory.getInUseAndRemove(beanName, type);
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
}
