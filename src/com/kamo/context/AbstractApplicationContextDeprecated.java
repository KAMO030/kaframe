package com.kamo.context;

import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.BeansException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.context.factory.*;
import com.kamo.util.Converter;
import com.kamo.util.ConverterRegistry;

import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Deprecated

public abstract class AbstractApplicationContextDeprecated implements ApplicationContext, BeanDefinitionRegistry {
//    protected final BeanMatcher exileBeanMatcher;
//    protected final Map<String, Object> singletonBeans;
//    protected final Map<String, BeanDefinition> beanDefinitions;
//    protected final Map<String, Object> exileBeans;
//    protected final Map<Class, Object> factoryBeans;
//    protected final Set<BeanFactoryPostProcessor> beanFactoryPostProcessors;
//    protected final Set<BeanInstanceProcessor> beanInstanceProcessors;
//    protected final Set<BeanPostProcessor> beanPostProcessors;
//
//    public AbstractApplicationContextDeprecated() {
//        singletonBeans = new ConcurrentHashMap<>();
//        beanDefinitions = new ConcurrentHashMap<>();
//        factoryBeans = new ConcurrentHashMap<>();
//        exileBeans = new ConcurrentHashMap<>();
//        beanFactoryPostProcessors = new LinkedHashSet<>();
//        beanInstanceProcessors = new LinkedHashSet<>();
//        beanPostProcessors = new LinkedHashSet<>();
//        exileBeanMatcher = new BeanMatcher(exileBeans);
//    }
//
//    @Override
//    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
//        if (!chekBeanName(beanName)) {
//            beanDefinitions.put(beanName, beanDefinition);
//            Class beanClass = beanDefinition.getBeanClass();
//            if (FactoryBean.class.isAssignableFrom(beanClass)) {
//                factoryBeans.put(beanClass, beanDefinition);
//            }
//        } else {
//            throw new BeanDefinitionStoreException();
//        }
//    }
//
//    private boolean chekBeanName(String beanName) {
//        return beanDefinitions.containsKey(beanName);
//    }
//
//    @Override
//    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
//        if (chekBeanName(beanName)) {
//            beanDefinitions.remove(beanName);
//        } else {
//            throw new NoSuchBeanDefinitionException();
//        }
//    }
//
//    public void refresh() {
//        //初始化注册BeanFactoryPostProcessor
//        registerBeanFactoryPostProcessors();
//
//        preInstantiateFactoryPostProcessors();
//        //执行BeanFactoryPostProcessor
//        invokeBeanFactoryPostProcessors();
//
//        preInstantiateSingletons();
//    }
//
//    public void registerBeanFactoryPostProcessors() {
//        singletonBeans.put(Introspector.decapitalize(this.getClass().getSimpleName()), this);
//        register(this.getClass());
//    }
//
//    private void preInstantiateFactoryPostProcessors() {
//        this.beanDefinitions.entrySet().forEach(beanDefinitionEntry -> {
//            registerConfiguration(beanDefinitionEntry.getKey(),
//                    beanDefinitionEntry.getValue());
//        });
//    }
//
//    private void invokeBeanFactoryPostProcessors() {
//        int size = beanFactoryPostProcessors.size();
//        beanFactoryPostProcessors.forEach(arryPostProce ->
//                arryPostProce.postProcessBeanFactory(this));
//        BeanFactoryPostProcessor[] arryPostProces = beanFactoryPostProcessors.toArray(new BeanFactoryPostProcessor[size]);
//        for (int i = size; i < arryPostProces.length; i++) {
//            arryPostProces[i].postProcessBeanFactory(this);
//        }
//        beanFactoryPostProcessors.forEach(beanFactoryPostProcessor -> {
//            if (beanFactoryPostProcessor instanceof BeanDefinitionRegistryPostProcessor) {
//                ((BeanDefinitionRegistryPostProcessor) beanFactoryPostProcessor)
//                        .postProcessBeanDefinitionRegistry(this);
//            }
//        });
//    }
//
//    private void preInstantiateSingletons() {
//        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitions.entrySet()) {
//            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
//            String beanName = beanDefinitionEntry.getKey();
//            if (!beanDefinition.isSingleton()) {
//                continue;
//            }
//            getSingletonBean(beanName, beanDefinition);
//        }
//    }
//
//    public BeanDefinition registerConfiguration(Class configClass) {
//        register(configClass);
//        String configName = Introspector.decapitalize(configClass.getSimpleName());
//        BeanDefinition beanDefinition = beanDefinitions.get(configName);
//        registerConfiguration(configName, beanDefinition);
//        return beanDefinition;
//    }
//
//    public void registerConfiguration(String beanName, BeanDefinition beanDefinition) {
//        if (!beanDefinitions.containsKey(beanName)) {
//            registerBeanDefinition(beanName, beanDefinition);
//        }
//        Class beanClass = beanDefinition.getBeanClass();
//        if (BeanFactoryPostProcessor.class.isAssignableFrom(beanClass)) {
//            beanFactoryPostProcessors.add((BeanFactoryPostProcessor)
//                    getSingletonBean(beanName, beanDefinition));
//        } else if (BeanInstanceProcessor.class.isAssignableFrom(beanClass)) {
//            beanInstanceProcessors.add((BeanInstanceProcessor)
//                    getSingletonBean(beanName, beanDefinition));
//        } else if (BeanPostProcessor.class.isAssignableFrom(beanClass)) {
//            beanPostProcessors.add((BeanPostProcessor)
//                    getSingletonBean(beanName, beanDefinition));
//        }else if (Converter.class.isAssignableFrom(beanClass)) {
//            ConverterRegistry.registerConverter( (Converter)getSingletonBean(beanName, beanDefinition));
//        }
//    }
//
//
//    public void register(Class... componentClasses) {
//
//    }
//
//    private Object creatBean(String beanName) {
//        BeanDefinition beanDefinition = beanDefinitions.get(beanName);
//        if (beanDefinition == null) {
//            throw new NoSuchBeanDefinitionException();
//        }
//        return beanDefinition.isSingleton()
//                ? getSingletonBean(beanName, beanDefinition)
//                : doCreatBean(beanName, beanDefinition);
//    }
//
//
//    protected Object doCreatBean(String beanName, BeanDefinition beanDefinition) {
//        Object bean = null;
//        try {
//            for (BeanInstanceProcessor instanceProcessor : beanInstanceProcessors) {
//                bean = instanceProcessor.instanceBefore(beanName, beanDefinition);
//                if (Objects.nonNull(bean)) {
//                    break;
//                }
//            }
//            if (Objects.isNull(bean)) {
//                bean = doInstantiate(beanName, beanDefinition);
//                for (BeanInstanceProcessor beanInstanceProcessor : beanInstanceProcessors) {
//                    beanInstanceProcessor.instanceAfter(beanName, beanDefinition, bean);
//                }
//                for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
//                    bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
//                }
//                if (bean instanceof InitializingBean) {
//                    ((InitializingBean) bean).afterPropertiesSet();
//                } else if (Objects.nonNull(beanDefinition.getInitMethodName())) {
//                    bean.getClass().getMethod(beanDefinition.getInitMethodName()).invoke(bean);
//                }
//                exileBeans.remove(beanName);
//            }
//            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
//                bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }
//
//    protected Object doInstantiate(String beanName, BeanDefinition beanDefinition) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
//        Object bean = beanDefinition.doInstance(getArgValues(beanDefinition));
//        if (bean == null) {
//            Constructor constructor = getConstructor(beanDefinition);
//            bean = constructor.getParameterCount() == 0
//                    ? constructor.newInstance()
//                    : constructor.newInstance(getArgValues(beanDefinition));
//        }
//        exileBeans.put(beanName, bean);
//        return bean;
//    }
//
//    private Object[] getArgValues(BeanDefinition beanDefinition) {
//        String[] argNames = beanDefinition.getArgNames();
//        if (argNames == null) {
//            return new Object[0];
//        }
//        Object[] argValues = new Object[argNames.length];
//        for (int i = 0; i < argNames.length; i++) {
//            Arguments args = beanDefinition.getArguments(argNames[i]);
//            Object value = args.getValue();
//            Class argsType = args.getType();
//            if (value == null) {
//                value = getBean(argNames[i], argsType);
//                args.setValue(value);
//            }else if (!argsType.isAssignableFrom(value.getClass())){
//                value = ConverterRegistry.convert(value,argsType);
//                args.setValue(value);
//            }
//            argValues[i] = value;
//        }
//        return argValues;
//    }
//
//    private Object getLazyInitBean(String beanName, BeanDefinition beanDefinition) {
//        return LazedProxy.getLazedProxy(beanDefinition.getBeanClass(),
//                () -> doCreatBean(beanName, beanDefinition));
//    }
//
//    public Object getSingletonBean(String beanName, BeanDefinition beanDefinition) {
//        if (singletonBeans.containsKey(beanName)) {
//            return getBean(beanName);
//        }
//        Object bean = beanDefinition.isLazyInit()
//                ? getLazyInitBean(beanName, beanDefinition)
//                : doCreatBean(beanName, beanDefinition);
//        if (bean instanceof FactoryBean) {
//            FactoryBean factoryBean = ((FactoryBean) bean);
//            factoryBeans.put(factoryBean.getObjectType(), factoryBean);
//        }
//        singletonBeans.put(beanName, bean);
//        return bean;
//    }
//
//    protected Constructor getConstructor(BeanDefinition beanDefinition) throws NoSuchMethodException {
//        Class beanClass = beanDefinition.getBeanClass();
//        String[] argNames = beanDefinition.getArgNames();
//        Constructor constructor = null;
//        try {
//            constructor = argNames != null ?
//                    getConstructorByArg(argNames, beanDefinition) :
//                    beanClass.getConstructor();
//        } catch (NoSuchMethodException e) {
//            try {
//                constructor = beanClass.getConstructor();
//            } catch (NoSuchMethodException ex) {
//                constructor = conjectureConstructor(beanDefinition);
//            }
//        }
//        return constructor;
//    }
//
//    private Constructor getConstructorByArg(String[] argNames, BeanDefinition beanDefinition) throws NoSuchMethodException {
//        Class[] paramTypes = new Class[argNames.length];
//        for (int i = 0; i < argNames.length; i++) {
//            Arguments arguments = beanDefinition.getArguments(argNames[i]);
//            paramTypes[i] = arguments.getType();
//        }
//        return beanDefinition.getBeanClass().getConstructor(paramTypes);
//    }
//
//    private Constructor conjectureConstructor(BeanDefinition beanDefinition) {
//        Constructor[] constructors = beanDefinition.getBeanClass().getConstructors();
//        boolean isTypeMatch = true;
//        for (Constructor constructor : constructors) {
//            isTypeMatch = true;
//            for (Class parameterType : constructor.getParameterTypes()) {
//                try {
//                    getBean(parameterType);
//                } catch (BeansException ex) {
//                    ex.printStackTrace();
//                    isTypeMatch = false;
//                    break;
//                }
//            }
//            if (isTypeMatch) {
//                beanDefinition.setArguments(constructor.getParameters());
//                return constructor;
//            }
//        }
//        throw new BeansException("");
//    }
//
//
//    @Override
//    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
//        if (chekBeanName(beanName)) {
//            return beanDefinitions.get(beanName);
//        } else {
//            throw new NoSuchBeanDefinitionException();
//        }
//    }
//
//    @Override
//    public boolean containsBeanDefinition(String beanName) {
//        return beanDefinitions.containsValue(beanName);
//    }
//
//    @Override
//    public String[] getBeanDefinitionNames() {
//        return beanDefinitions.keySet().toArray(new String[0]);
//    }
//
//    @Override
//    public int getBeanDefinitionCount() {
//        return beanDefinitions.size();
//    }
//
//    @Override
//    public boolean isSingletonCurrentlyInitialized(String beanName) {
//        return false;
//    }
//
//    @Override
//    public boolean isInUse(Class type) {
//        return exileBeanMatcher.match(type);
//    }
//
//    @Override
//    public Object getInUseBean(String beanName, Class type) {
//        return exileBeanMatcher.getMatch(beanName, type);
//    }
//
//    @Override
//    public boolean containsBeanB(String name) {
//        return beanDefinitions.containsKey(name);
//    }
//
//    @Override
//    public String[] getAliases(String name) {
//        return new String[0];
//    }
//
//    @Override
//    public <T> T getBean(Class<T> requiredType) {
//
//        String[] matchNames = getBeanNamesByType(requiredType);
//        if (matchNames.length > 1) {
//            throw new BeanDefinitionStoreException("存在多个类型为: " + requiredType.getName() + " 的 Bean");
//        } else if (matchNames.length == 0) {
//            T bean = getBeanByFactory(Introspector.decapitalize(requiredType.getName()), requiredType);
//            if (bean != null) {
//                return bean;
//            }
//            bean = getBeanByConverter(Introspector.decapitalize(requiredType.getName()),requiredType);
//            if (bean != null) {
//                return bean;
//            }
//            throw new NoSuchBeanDefinitionException("找不到类型为: " + requiredType.getName() + " 的 Bean");
//        }
//        return (T) getBean(matchNames[0]);
//    }
//
//    public String[] getBeanNamesByType(Class requiredType) {
//        String[] beanDefinitionNames = getBeanDefinitionNames();
//        List<String> matchNames = new ArrayList<>();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            if (isTypeMatch(beanDefinitionName, requiredType)) {
//                matchNames.add(beanDefinitionName);
//            }
//        }
//        return matchNames.toArray(new String[0]);
//    }
//
//    public <T> T getBeanByFactory(String name, Class<T> type) {
//        Object factory = factoryBeans.get(type);
//        if (factory == null) {
//            for (Class factoryType : factoryBeans.keySet()) {
//                if (factoryType.isAssignableFrom(type)) {
//                    factory = factoryBeans.get(factoryType);
//                }
//            }
//            if (factory == null) {
//                return null;
//            }
//        }
//        FactoryBean factoryBean = factory instanceof BeanDefinition ?
//                (FactoryBean) getSingletonBean(factory.getClass().getSimpleName(), (BeanDefinition) factory) :
//                (FactoryBean) factory;
//        T object = (T) factoryBean.getObject(type);
//        if (factoryBean.isSingleton()) {
//            singletonBeans.put(name, object);
//        }
//        return object;
//    }
//
//
//    @Override
//    public <T> T getBean(Class<T> requiredType, Object... args) {
//        return null;
//    }
//
//    @Override
//    public <T extends Object> T getBean(String name) {
//        Object bean = null;
//        if (!containsBean(name)) {
//            throw new NoSuchBeanDefinitionException("找不到ID为: " + name + " 的 Bean");
//        }
//        if (singletonBeans.containsKey(name)) {
//            bean = singletonBeans.get(name);
//        } else {
//            bean = creatBean(name);
//        }
//        if (bean instanceof FactoryBean) {
//            FactoryBean factoryBean = (FactoryBean) bean;
//            bean = getBeanByFactory(name, factoryBean.getObjectType());
//        }
//
//        return (T) bean;
//    }
//
//    @Override
//    public <T> T getBean(String name, Class<T> requiredType) {
//        T bean = null;
//        String[] matchNames = getBeanNamesByType(requiredType);
//        if (matchNames.length == 0) {
//            bean = getBeanByFactory(name, requiredType);
//            if (bean != null) {
//                return bean;
//            }
//            bean = getBeanByConverter(name,requiredType);
//            if (bean != null) {
//                return bean;
//            }
//            throw new NoSuchBeanDefinitionException("找不到类型为: " + requiredType.getName() + " 的 Bean");
//        }
//        if (matchNames.length == 1) {
//            bean =  getBean(matchNames[0]);
//        } else {
//            for (String matchName : matchNames) {
//                if (name.equals(matchName)) {
//                    bean =  getBean(matchName);
//                    break;
//                }
//            }
//        }
//        if (bean == null) {
//            throw new NoSuchBeanDefinitionException(" 找到了多个类型为: " + requiredType.getName() + " 的 bean: " + Arrays.toString(matchNames) + " 而当前需要的名字为:" + name);
//        }
//        return bean;
//    }
//
//    public <T> T getBeanByConverter(String name, Class<T> requiredType){
//        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitions.entrySet()) {
//            Class beanClass = beanDefinitionEntry.getValue().getBeanClass();
//
//            Converter converter = ConverterRegistry.getConverter(requiredType, beanClass);
//            if (converter != null) {
//                Object bean;
//                try {
//                    bean = getBean(name,beanClass);
//                }catch (NoSuchBeanDefinitionException e) {
//                  throw  new NoSuchBeanDefinitionException("尝试将:"+beanClass+" 类型转换到: "+requiredType+" 类型时,"+e.getMessage());
//                }
//                return  (T) converter.convert(bean);
//            }
//        }
//
//        return null;
//    }
//    @Override
//    public Object getBean(String name, Object... args) {
//        if (singletonBeans.containsKey(name)) {
//            return singletonBeans.get(name);
//        }
//        if (containsBean(name)) {
//            throw new NoSuchBeanDefinitionException();
//        }
//        BeanDefinition beanDefinition = beanDefinitions.get(name);
//        for (Object arg : args) {
//            Class<?> argClass = arg.getClass();
//            Arguments arguments = new Arguments(Introspector.decapitalize(argClass.getName()),
//                    argClass, args);
//            beanDefinition.addArguments(arguments.getName(), arguments);
//        }
//
//        return creatBean(name);
//    }
//
//    @Override
//    public Class<?> getType(String name) {
//        return getType(name, false);
//    }
//
//    @Override
//    public Class<?> getType(String name, boolean allowFactoryBeanInit) {
//        if (!containsBean(name)) {
//            throw new NoSuchBeanDefinitionException();
//        }
//        BeanDefinition beanDefinition = beanDefinitions.get(name);
//        Class beanClass = beanDefinition.getBeanClass();
//        if (allowFactoryBeanInit) {
//            getBean(beanClass);
//        }
//        return beanClass;
//    }
//
//    @Override
//    public boolean isPrototype(String name) {
//        return beanDefinitions
//                .get(name)
//                .getScope()
//                .equalsIgnoreCase(BeanDefinition.SCOPE_PROTOTYPE);
//    }
//
//    @Override
//    public boolean isSingleton(String name) {
//        return beanDefinitions
//                .get(name)
//                .isSingleton();
//    }
//
//    @Override
//    public boolean isTypeMatch(String name, Class typeToMatch) {
//        if (!containsBean(name)) {
//            return false;
//        }
//        return typeToMatch
//                .isAssignableFrom(getBeanDefinition(name)
//                        .getBeanClass());
//    }
//
//    @Override
//    public Object[] getBeans() {
//        return singletonBeans.values().toArray();
//    }
}
