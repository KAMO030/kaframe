package com.kamo.context;

import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.BeansException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.context.factory.*;


import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractApplicationContext implements ApplicationContext, BeanDefinitionRegistry {
    protected final BeanMatcher exileBeanMatcher;
    protected final Map<String, Object> singletonBeans;
    protected final Map<String, BeanDefinition> beanDefinitions;
    protected final Map<String,Object> exileBeans;
    protected final Map<Class, Object> factoryBeans;
    protected final List<BeanFactoryPostProcessor> beanFactoryPostProcessors;
    protected final List<BeanInstanceProcessor> beanInstanceProcessors;
    protected final List<BeanPostProcessor> beanPostProcessors;

    public AbstractApplicationContext() {
        singletonBeans = new ConcurrentHashMap<>();
        beanDefinitions = new ConcurrentHashMap<>();
        factoryBeans = new ConcurrentHashMap<>();
        exileBeans = new ConcurrentHashMap<>();
        beanFactoryPostProcessors = new ArrayList<>();
        beanInstanceProcessors = new ArrayList<>();
        beanPostProcessors = new ArrayList<>();
        exileBeanMatcher = new BeanMatcher(exileBeans);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        if (!chekBeanName(beanName)) {
            beanDefinitions.put(beanName, beanDefinition);
            Class beanClass = beanDefinition.getBeanClass();
            if (FactoryBean.class.isAssignableFrom(beanClass)) {
                factoryBeans.put(beanClass, beanDefinition);
            }
        } else {
            throw new BeanDefinitionStoreException();
        }
    }

    private boolean chekBeanName(String beanName) {
        return beanDefinitions.containsKey(beanName);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (chekBeanName(beanName)) {
            beanDefinitions.remove(beanName);
        } else {
            throw new NoSuchBeanDefinitionException();
        }
    }

    public void refresh() {
        //初始化注册BeanFactoryPostProcessor
        registerBeanFactoryPostProcessors();
        //执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors();

        preInstantiateSingletons();
    }

    private void preInstantiateSingletons() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitions.entrySet()) {
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinition.isSingleton()) {
                continue;
            }
            getSingletonBean(beanName, beanDefinition);
        }
    }

    private Object creatBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitions.get(beanName);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException();
        }
        return beanDefinition.isSingleton()
                ? getSingletonBean(beanName, beanDefinition)
                : doCreatBean(beanName, beanDefinition);
    }


    protected Object doCreatBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            for (BeanInstanceProcessor instanceProcessor : beanInstanceProcessors) {
                bean = instanceProcessor.instanceBefore(beanName, beanDefinition);
                if (bean != null) {
                    break;
                }
            }
            if (bean == null) {
                bean = doInstantiate(beanName, beanDefinition);
                for (BeanInstanceProcessor beanInstanceProcessor : beanInstanceProcessors) {
                    beanInstanceProcessor.instanceAfter(beanName, beanDefinition, bean);
                }
                for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                    bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
                }
                if (bean instanceof InitializingBean) {
                    ((InitializingBean) bean).afterPropertiesSet();
                } else if (beanDefinition.getInitMethodName() != null) {
                    bean.getClass().getMethod(beanDefinition.getInitMethodName()).invoke(bean);
                }
                exileBeans.remove(beanName);
            }
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    protected Object doInstantiate(String beanName, BeanDefinition beanDefinition) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Object bean = beanDefinition.doInstance(getArgValues(beanDefinition));
        if (bean == null) {
            Constructor constructor = getConstructor(beanDefinition);
            bean = constructor.getParameterCount() == 0
                    ? constructor.newInstance()
                    : constructor.newInstance(getArgValues(beanDefinition));
        }
        exileBeans.put(beanName,bean);
        return bean;
    }

    private Object[] getArgValues(BeanDefinition beanDefinition) {
        String[] argNames = beanDefinition.getArgNames();
        if (argNames == null) {
            return new Object[0];
        }
        Object[] argValues  = new Object[argNames.length];
        for (int i = 0; i < argNames.length; i++) {
            Arguments args = beanDefinition.getArguments(argNames[i]);
            Object value = args.getValue();
            if (value == null) {
                value = getBean(argNames[i], args.getType());
                args.setValue(value);
            }
            argValues[i] = value;
        }
        return argValues;
    }

    private Object getLazyInitBean(String beanName, BeanDefinition beanDefinition) {
        return LazedProxy.getLazedProxy(beanDefinition.getBeanClass(),
                () -> doCreatBean(beanName, beanDefinition));
    }

    private Object getSingletonBean(String beanName, BeanDefinition beanDefinition) {
        if (singletonBeans.containsKey(beanName)) {
            return getBean(beanName);
        }
        Object bean = beanDefinition.isLazyInit()
                ? getLazyInitBean(beanName, beanDefinition)
                : doCreatBean(beanName, beanDefinition);
        if (bean instanceof FactoryBean) {
            FactoryBean factoryBean = ((FactoryBean) bean);
            factoryBeans.put(factoryBean.getObjectType(), factoryBean);
        }
        singletonBeans.put(beanName, bean);
        return bean;
    }

    protected Constructor getConstructor(BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class beanClass = beanDefinition.getBeanClass();
        try {
            return beanClass.getConstructor();
        } catch (NoSuchMethodException e) {
            return conjectureConstructor(beanDefinition);
        }
    }

    private Constructor conjectureConstructor(BeanDefinition beanDefinition) {
        Constructor[] constructors = beanDefinition.getBeanClass().getConstructors();
        boolean isTypeMatch = true;
        for (Constructor constructor : constructors) {
            isTypeMatch = true;
            for (Class parameterType : constructor.getParameterTypes()) {
                try {
                    getBean(parameterType);
                }catch (BeansException ex) {
                    ex.printStackTrace();
                    isTypeMatch = false;
                    break;
                }
            }
            if (isTypeMatch) {
                beanDefinition.setArguments(constructor.getParameters());
                return constructor;
            }
        }
        throw new BeansException("");
    }

    private void invokeBeanFactoryPostProcessors() {
        int size = beanFactoryPostProcessors.size();
        for (int i = 0; i < beanFactoryPostProcessors.size(); i++) {
            beanFactoryPostProcessors.get(i).postProcessBeanFactory(this);
        }
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessors) {
            if (beanFactoryPostProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                ((BeanDefinitionRegistryPostProcessor) beanFactoryPostProcessor).postProcessBeanDefinitionRegistry(this);
            }
        }
    }

    public void registe(Class<?> clazz) {

    }

    @Override
    public void registerConfiguration(Object regist) {
        if (regist instanceof BeanFactoryPostProcessor) {
            beanFactoryPostProcessors.add((BeanFactoryPostProcessor) regist);
        } else if (regist instanceof BeanInstanceProcessor) {
            beanInstanceProcessors.add((BeanInstanceProcessor) regist);
        } else if (regist instanceof BeanPostProcessor) {
            beanPostProcessors.add((BeanPostProcessor) regist);
        } else if (regist instanceof Class) {
            register((Class) regist);
        }

    }

    public void register(Class... componentClasses) {

    }

    public void registerBeanFactoryPostProcessors() {
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        if (chekBeanName(beanName)) {
            return beanDefinitions.get(beanName);
        } else {
            throw new NoSuchBeanDefinitionException();
        }
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitions.containsValue(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitions.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitions.size();
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return false;
    }

    @Override
    public boolean isInUse( Class type) {
        return exileBeanMatcher.match(type);
    }

    @Override
    public Object getInUseAndRemove(String beanName,Class type) {
        return exileBeanMatcher.getMatchAndRemove(beanName,type);
    }

    @Override
    public boolean containsBean(String name) {
        return beanDefinitions.containsKey(name);
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {

        String[] matchNames = getBeanNamesByType(requiredType);
        if (matchNames.length > 1) {
            throw new BeanDefinitionStoreException("存在多个类型为: " + requiredType.getName() + " 的 Bean");
        } else if (matchNames.length == 0) {
            T bean = getBeanByFactory(requiredType);
            if (bean != null) {
                return bean;
            }

            throw new NoSuchBeanDefinitionException("找不到类型为: " + requiredType.getName() + " 的 Bean");
        }
        return (T) getBean(matchNames[0]);
    }

    public String[] getBeanNamesByType(Class requiredType) {
        String[] beanDefinitionNames = getBeanDefinitionNames();
        List<String> matchNames = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (isTypeMatch(beanDefinitionName, requiredType)) {
                matchNames.add(beanDefinitionName);
            }

        }
        return matchNames.toArray(new String[0]);
    }

    public <T> T getBeanByFactory(Class<T> type) {
        Object factory =  factoryBeans.get(type);
        if (factory == null) {
            for (Class factoryType : factoryBeans.keySet()) {
                if (factoryType.isAssignableFrom(type)) {
                    factory = factoryBeans.get(factoryType);
                }
            }
            if (factory == null){
                return null;
            }
        }
        FactoryBean factoryBean = factory instanceof BeanDefinition?
                (FactoryBean) getSingletonBean(factory.getClass().getSimpleName(), (BeanDefinition) factory):
                (FactoryBean) factory;
        T object = (T) factoryBean.getObject(type);
        return object;
    }


    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return null;
    }

    @Override
    public Object getBean(String name) {
        Object bean = null;
        if (!containsBean(name)) {
            throw new NoSuchBeanDefinitionException("找不到ID为: " + name + " 的 Bean");
        }
        if (singletonBeans.containsKey(name)) {
            bean = singletonBeans.get(name);
        } else {
            bean = creatBean(name);
        }
        if (bean instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean) bean;
            bean = getBeanByFactory(factoryBean.getObjectType());

        }
        return bean;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        T bean = null;
        String[] matchNames = getBeanNamesByType(requiredType);
        if (matchNames.length == 0) {
            bean = getBeanByFactory(requiredType);
            if (bean != null) {
                return bean;
            }
            throw new NoSuchBeanDefinitionException("找不到类型为: " + requiredType.getName() + " 的 Bean");
        }
        if (matchNames.length == 1) {
            bean = (T) getBean(matchNames[0]);
        } else {
            for (String matchName : matchNames) {
                if (name.equals(matchName)) {
                    bean = (T) getBean(matchName);
                    break;
                }
            }
        }
        if (bean == null) {
            throw new NoSuchBeanDefinitionException("找不到ID为: " + name + " 的 Bean");
        }
        return bean;
    }

    @Override
    public Object getBean(String name, Object... args) {
        if (singletonBeans.containsKey(name)) {
            return singletonBeans.get(name);
        }
        if (containsBean(name)) {
            throw new NoSuchBeanDefinitionException();
        }
        BeanDefinition beanDefinition = beanDefinitions.get(name);
        for (Object arg : args) {
            Class<?> argClass = arg.getClass();
            Arguments arguments = new Arguments(Introspector.decapitalize(argClass.getName()),
                    argClass, args);
            beanDefinition.addArguments(arguments.getName(), arguments);
        }

        return creatBean(name);
    }

    @Override
    public Class<?> getType(String name) {
        return getType(name, false);
    }

    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit) {
        if (!containsBean(name)) {
            throw new NoSuchBeanDefinitionException();
        }
        BeanDefinition beanDefinition = beanDefinitions.get(name);
        Class beanClass = beanDefinition.getBeanClass();
        if (allowFactoryBeanInit) {
            getBean(beanClass);
        }
        return beanClass;
    }

    @Override
    public boolean isPrototype(String name) {
        return beanDefinitions
                .get(name)
                .getScope()
                .equalsIgnoreCase(BeanDefinition.SCOPE_PROTOTYPE);
    }

    @Override
    public boolean isSingleton(String name) {
        return beanDefinitions
                .get(name)
                .isSingleton();
    }

    @Override
    public boolean isTypeMatch(String name, Class typeToMatch) {
        if (!containsBean(name)) {
            return false;
        }
        return typeToMatch
                .isAssignableFrom(getBeanDefinition(name)
                        .getBeanClass());
    }

    @Override
    public Object[] getBeans() {
        return singletonBeans.values().toArray();
    }
}
