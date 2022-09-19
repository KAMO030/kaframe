package com.kamo.context;

import com.kamo.bean.BeanDefinition;
import com.kamo.bean.InitializingBean;
import com.kamo.bean.support.Arguments;
import com.kamo.bean.support.BeanDefinitionBuilder;
import com.kamo.context.converter.Converter;
import com.kamo.context.converter.ConverterRegistry;
import com.kamo.context.exception.BeanDefinitionStoreException;
import com.kamo.context.exception.BeansException;
import com.kamo.context.exception.NoSuchBeanDefinitionException;
import com.kamo.context.factory.*;
import com.kamo.context.listener.ApplicationEventMulticaster;
import com.kamo.context.listener.ApplicationListener;
import com.kamo.core.support.impl.LazedInvocationHandler;
import com.kamo.core.util.ProxyUtils;
import com.kamo.core.util.ReflectUtils;
import com.kamo.proxy.AdvisorRegister;

import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory extends DefaultBeanDefinitionRegistry implements BeanFactory {
    protected final BeanMatcher exileBeanMatcher;
    protected final Map<String, Object> singletonBeans;


    protected ClassLoader classLoader;
    protected final Map<String, Object> exileBeans;
    protected final Map<Class, Object> factoryBeans;


    public AbstractBeanFactory() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public AbstractBeanFactory(ClassLoader classLoader) {

        singletonBeans = new ConcurrentHashMap<>();
        exileBeans = new ConcurrentHashMap<>();
        exileBeanMatcher = new BeanMatcher(exileBeans);
        factoryBeans = this.getFactoryBeans();
        this.classLoader = classLoader;
    }

    private Object creatBean(String beanName) {
        BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException();
        }
        return beanDefinition.isSingleton()
                ? getSingletonBean(beanName, beanDefinition)
                : doCreatBean(beanName, beanDefinition);
    }

    protected abstract Object applyBeanInstanceBeforeProcessor(String beanName, BeanDefinition beanDefinition);

    protected abstract void applyBeanInstanceAfterProcessor(String beanName, BeanDefinition beanDefinition, Object bean);

    protected abstract Object applyBeanPostProcessorBefore(Object bean, String beanName);

    protected abstract Object applyBeanPostProcessorAfter(Object bean, String beanName);

    protected Object doCreatBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {

            bean = applyBeanInstanceBeforeProcessor(beanName, beanDefinition);

            if (Objects.isNull(bean)) {

                bean = doInstantiate(beanName, beanDefinition);

                applyBeanInstanceAfterProcessor(beanName, beanDefinition, bean);

                bean = applyBeanPostProcessorBefore(bean, beanName);

                if (bean instanceof InitializingBean) {
                    ((InitializingBean) bean).afterPropertiesSet();
                } else if (Objects.nonNull(beanDefinition.getInitMethodName())) {
                    ReflectUtils.invokeMethod(bean, beanDefinition.getInitMethodName());
                }

                exileBeans.remove(beanName);
            }

            bean = applyBeanPostProcessorAfter(bean, beanName);

            doAware(bean);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    private void doAware(Object bean) {
        if (bean instanceof ApplicationListener) {
            String[] multicasterNames = ((ApplicationListener<?>) bean).getMulticasterNames();
            Arrays.stream(multicasterNames).forEach(name -> {
                getBean(name, ApplicationEventMulticaster.class)
                        .addApplicationListener((ApplicationListener) bean);
            });
        }
        if (!(bean instanceof Aware)) {
            return;
        }
        Aware aware = (Aware) bean;
        Object[] awareValues = aware.getAwareValues(this);
        aware.setAware(awareValues);
//        if (bean instanceof ApplicationPublisherAware) {
//            ((ApplicationPublisherAware) bean).setApplicationPublisher(applicationContext);
//        }
//        if (bean instanceof ApplicationContextAware) {
//            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
//        }
//        if (bean instanceof ClassLoadAware) {
//            ((ClassLoadAware) bean).setClassLoad(classLoader);
//        }
//        if (bean instanceof ApplicationEventMulticasterAware) {
//            ((ApplicationEventMulticasterAware) bean).setApplicationEventMulticaster(
//                    getBean(DefaultEventMulticaster.DEFAULT_EVENT_MULTICASTER_NAME,
//                            ApplicationEventMulticaster.class));
//        }
    }

    protected Object doInstantiate(String beanName, BeanDefinition beanDefinition) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Object bean = beanDefinition.doInstance(getArgValues(beanDefinition));
        if (bean == null) {
            Constructor constructor = getConstructor(beanDefinition);
            bean = constructor.getParameterCount() == 0
                    ? constructor.newInstance()
                    : constructor.newInstance(getArgValues(beanDefinition));
        }
        exileBeans.put(beanName, bean);
        return bean;
    }

    private Object[] getArgValues(BeanDefinition beanDefinition) {
        String[] argNames = beanDefinition.getArgNames();
        if (argNames == null) {
            return new Object[0];
        }
        Object[] argValues = new Object[argNames.length];
        for (int i = 0; i < argNames.length; i++) {
            Arguments args = beanDefinition.getArguments(argNames[i]);
            Object value = args.getValue();
            Class argsType = args.getType();
            if (value == null) {
                value = getBean(argNames[i], argsType);
                args.setValue(value);
            } else if (!argsType.isAssignableFrom(value.getClass())) {
                value = ConverterRegistry.convert(value, argsType);
                args.setValue(value);
            }
            argValues[i] = value;
        }
        return argValues;
    }


    public Object getSingletonBean(String beanName, BeanDefinition beanDefinition) {
        if (singletonBeans.containsKey(beanName)) {
            return getBean(beanName);
        }
        Object bean = doCreatBean(beanName, beanDefinition);
        if (bean instanceof FactoryBean) {
            FactoryBean factoryBean = ((FactoryBean) bean);
            factoryBeans.put(factoryBean.getObjectType(), factoryBean);
        }
        Objects.requireNonNull(bean, "创建:" + beanName + "时发生错误");
        singletonBeans.put(beanName, bean);
        return bean;
    }

    public void addSingletonBean(String name, Object bean) {
        this.singletonBeans.put(name, bean);
        if (this.containsBeanDefinition(name)) {
            return;
        }
        BeanDefinition beanDefinition = BeanDefinitionBuilder.getBeanDefinition(bean.getClass());
        this.registerBeanDefinition(name, beanDefinition);
    }

    @Override
    public void destroy() {
        singletonBeans.forEach((name, bean) -> {
            String destroyMethodName = this.getBeanDefinition(name).getDestroyMethodName();
            if (destroyMethodName != null) {
                ReflectUtils.invokeMethod(bean, destroyMethodName);
            }
        });
        singletonBeans.clear();
    }

    protected Constructor getConstructor(BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();
        String[] argNames = beanDefinition.getArgNames();
        Constructor constructor = null;
        try {
            constructor = argNames != null ?
                    getConstructorByArg(argNames, beanDefinition) :
                    beanClass.getConstructor();
        } catch (NoSuchMethodException e) {
            try {
                constructor = beanClass.getConstructor();
            } catch (NoSuchMethodException ex) {
                constructor = conjectureConstructor(beanDefinition);
            }
        }
        return constructor;
    }

    private Constructor getConstructorByArg(String[] argNames, BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class[] paramTypes = new Class[argNames.length];
        for (int i = 0; i < argNames.length; i++) {
            Arguments arguments = beanDefinition.getArguments(argNames[i]);
            paramTypes[i] = arguments.getType();
        }
        return beanDefinition.getBeanClass().getConstructor(paramTypes);
    }

    private Constructor conjectureConstructor(BeanDefinition beanDefinition) {
        Constructor[] constructors = beanDefinition.getBeanClass().getConstructors();
        boolean isTypeMatch = true;
        for (Constructor constructor : constructors) {
            isTypeMatch = true;
            for (Class parameterType : constructor.getParameterTypes()) {
                try {
                    getBean(parameterType);
                } catch (BeansException ex) {
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

    @Override
    public boolean isUsing(Class type) {
        return exileBeanMatcher.match(type);
    }


    @Override
    public void addSingletonBeans(Object... beans) {
        for (Object bean : beans) {
            String beanName = Introspector.decapitalize(bean.getClass().getSimpleName());
            addSingletonBean(beanName, bean);
        }
    }

    protected Object getUsingBean(String beanName, Class type) {
        return exileBeanMatcher.getMatch(beanName, type);
    }



    @Override
    public boolean containSingletonBean(String name) {
        return this.singletonBeans.containsKey(name);
    }


    @Override
    public boolean containsBeanDefinition(Class type) {
        return getBeanNamesByType(type).length > 0;
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        String[] matchNames = getBeanNamesByType(requiredType);
        String name = requiredType.getName();
        if (matchNames.length > 1) {
            throw new BeanDefinitionStoreException("存在多个类型为: " + name + " 的 Bean");
        } else if (matchNames.length == 0) {
            T bean = getBeanByFactory(Introspector.decapitalize(name), requiredType);
            if (bean != null) {
                return bean;
            }
            bean = getBeanByConverter(Introspector.decapitalize(name), requiredType);
            if (bean != null) {
                return bean;
            }
            throw new NoSuchBeanDefinitionException("找不到类型为: " + name + " 的 Bean");
        }
        return (T) getBean(matchNames[0]);
    }

    @Override
    public String[] getBeanNamesByType(Class requiredType) {
        String[] beanDefinitionNames = this.getBeanDefinitionNames();
        List<String> matchNames = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (isTypeMatch(beanDefinitionName, requiredType)) {
                matchNames.add(beanDefinitionName);
            }
        }
        return matchNames.toArray(new String[0]);
    }

    public <T> T getBeanByFactory(String name, Class<T> type) {
        FactoryBean factoryBean = (FactoryBean) factoryBeans.get(type);
        if (factoryBean == null) {
            for (Class factoryClass : factoryBeans.keySet()) {
                Object beanDefinitionObj = factoryBeans.get(factoryClass);
                if (beanDefinitionObj instanceof BeanDefinition) {
                    String factoryClassName = factoryClass.getSimpleName();
                    BeanDefinition beanDefinition = (BeanDefinition) beanDefinitionObj;
                    FactoryBean factory = (FactoryBean) getSingletonBean(Introspector.decapitalize(factoryClassName), beanDefinition);
                    Class objectType = factory.getObjectType();
                    factoryBeans.remove(factoryClass);
                    factoryBeans.put(objectType, factory);
                    if (objectType.isAssignableFrom(type)) {
                        factoryBean = factory;
                        break;
                    }
                } else {
                    factoryBean = (FactoryBean) beanDefinitionObj;
                }
            }
            if (factoryBean == null) {
                return null;
            }
        }
        Object factoryObject = factoryBean.getObject(type);
        if (factoryBean.isSingleton()) {
            singletonBeans.put(name, factoryObject);
        }
        return (T) factoryObject;
    }

    protected <T> T getBean(String name, boolean isInterface) {
        Object bean = null;
        if (!containsBeanDefinition(name)) {
            throw new NoSuchBeanDefinitionException("找不到ID为: " + name + " 的 Bean");
        }
        if (singletonBeans.containsKey(name)) {
            bean = singletonBeans.get(name);
        } else {

            BeanDefinition beanDefinition = this.getBeanDefinition(name);
            Class beanClass = beanDefinition.getBeanClass();
            if (isUsing(beanDefinition.getBeanClass())) {
                //如果需要的此类型正在创建，出现了循环依赖
                if (isNeedProxy(name, beanClass)) {
                    InvocationHandler lazedInvocationHandler = new LazedInvocationHandler(() -> getBean(name, beanClass));
                    return (T) ProxyUtils.creatProxyInstance(beanClass, lazedInvocationHandler);
                }
                return (T) this.getUsingBean(name, beanClass);
            }
            bean = creatBean(name);
            if (bean instanceof FactoryBean) {
                FactoryBean factoryBean = (FactoryBean) bean;
                bean = getBeanByFactory(name, factoryBean.getObjectType());
            }
        }
        return (T) bean;
    }

    private boolean isNeedProxy(String name, Class beanClass) {
        if (beanClass.isInterface()) {
            return AdvisorRegister.classFilter(this.getBeanDefinition(name).getBeanClass());
        }
        return AdvisorRegister.classFilter(beanClass);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return null;
    }

    @Override
    public <T extends Object> T getBean(String name) {
        return getBean(name, false);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        T bean = null;
        String[] matchNames = getBeanNamesByType(requiredType);
        if (matchNames.length == 0) {
            bean = getBeanByFactory(name, requiredType);
            if (bean != null) {
                return bean;
            }
            bean = getBeanByConverter(name, requiredType);
            if (bean != null) {
                return bean;
            }
            throw new NoSuchBeanDefinitionException("找不到类型为: " + requiredType.getName() + " 的 Bean");
        }
        if (matchNames.length == 1) {
            bean = getBean(matchNames[0], requiredType.isInterface());
        } else {
            for (String matchName : matchNames) {
                if (matchName.equals(name)) {
                    bean = getBean(matchName, requiredType.isInterface());
                    break;
                }
            }
        }
        if (bean == null) {
            throw new NoSuchBeanDefinitionException(" 找到了多个类型为: " + requiredType.getName() + " 的 bean: " + Arrays.toString(matchNames) + " 而当前需要的名字为:" + name);
        }
        return bean;
    }

    public <T> T getBeanByConverter(String name, Class<T> requiredType) {
        for (String beanDefinitionName : this.getBeanDefinitionNames()) {
            Class beanClass = this.getBeanDefinition(beanDefinitionName).getBeanClass();

            Converter converter = ConverterRegistry.getConverter(requiredType, beanClass);
            if (converter != null) {
                Object bean;
                try {
                    bean = getBean(name, beanClass);
                } catch (NoSuchBeanDefinitionException e) {
                    throw new NoSuchBeanDefinitionException("尝试将:" + beanClass + " 类型转换到: " + requiredType + " 类型时," + e.getMessage());
                }
                return (T) converter.convert(bean);
            }
        }

        return null;
    }

    @Override
    public Object getBean(String name, Object... args) {
        if (singletonBeans.containsKey(name)) {
            return singletonBeans.get(name);
        }
        if (containsBeanDefinition(name)) {
            throw new NoSuchBeanDefinitionException();
        }
        BeanDefinition beanDefinition = this.getBeanDefinition(name);
        for (Object arg : args) {
            Class<?> argClass = arg.getClass();
            Arguments arguments = new Arguments(Introspector.decapitalize(argClass.getSimpleName()),
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
        if (!containsBeanDefinition(name)) {
            throw new NoSuchBeanDefinitionException();
        }
        BeanDefinition beanDefinition = this.getBeanDefinition(name);
        Class beanClass = beanDefinition.getBeanClass();
        if (allowFactoryBeanInit) {
            getBean(beanClass);
        }
        return beanClass;
    }

    @Override
    public boolean isPrototype(String name) {
        return this.getBeanDefinition(name).getScope()
                .equalsIgnoreCase(BeanDefinition.SCOPE_PROTOTYPE);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.getBeanDefinition(name).isSingleton();
    }

    @Override
    public boolean isTypeMatch(String name, Class typeToMatch) {
        return containsBeanDefinition(name) ?
                typeToMatch.isAssignableFrom(this.getBeanDefinition(name).getBeanClass())
                : false;
    }

    @Override
    public Object[] getBeans() {
        return singletonBeans.values().toArray();
    }

    @Override
    public <T> List<T> getBeans(Class<T> requiredType) {
        String[] beanNamesByType = getBeanNamesByType(requiredType);
        List<T> beanList = new ArrayList<>(beanNamesByType.length);
        for (String name : beanNamesByType) {
            beanList.add(getBean(name));
        }
        return beanList;
    }


}
