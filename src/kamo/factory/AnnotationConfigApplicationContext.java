package kamo.factory;






import kamo.context.annotation.Import;
import kamo.proxy.TransactionProxy;
import kamo.transaction.impl.TransManagerImpl;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private String rootPath;
    private Map<String, BeanDefinition> beanDefinitionMap;
    private Map<String, Object> singletonBeanMap;
    private Map<Object, Method[]> configMethodMap;

    public AnnotationConfigApplicationContext(Class configClass) {
        if (!configClass.isAnnotationPresent(Configuration.class)) {
            throw new ClassCastException("该类不是Configuration");
        }
        beanDefinitionMap = new ConcurrentHashMap<>();
        singletonBeanMap = new ConcurrentHashMap<>();
        configMethodMap = new ConcurrentHashMap<>();
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            scanConfig(configClass);
        }
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals(Scope.SINGLETON)) {
                singletonBeanMap.put(beanName, creadBean(beanName, beanDefinition));
            }
        }
        try {
            List<Method> methodList = new ArrayList<>();
            for (Method mm:configClass.getMethods()){
                if (mm.isAnnotationPresent(Bean.class)){
                    methodList.add(mm);
                }
            }
            configMethodMap.put(configClass.getDeclaredConstructor().newInstance(), methodList.toArray(new Method[0]));
            if (configClass.isAnnotationPresent(Import.class)) {
                Import annotation = (Import) configClass.getAnnotation(Import.class);
                Class[] classes = annotation.value();
                for (Class clazz : classes) {
                    if (!clazz.isAnnotationPresent(Configuration.class)) {
                        throw new ClassCastException(clazz.getName() + "该类不是Configuration");
                    }else if (clazz==configClass){
                        continue;
                    }
                    Object importer = clazz.getDeclaredConstructor().newInstance();
                    methodList= new ArrayList<>();
                    for (Method mm:clazz.getDeclaredMethods()){
                        if (mm.isAnnotationPresent(Bean.class)||mm.isAnnotationPresent(Aspect.class)){
                            mm.setAccessible(true);
                            methodList.add(mm);
                        }
                    }
                    configMethodMap.put(importer, methodList.toArray(new Method[0]));
                }
            }
            for (Object configBean : configMethodMap.keySet()) {
                Method[] methods = configMethodMap.get(configBean);
                creatAsp(configBean, methods);
                loadCofig(configBean, methods);
            }
            for (String beanName :singletonBeanMap.keySet()){
                Object bean = singletonBeanMap.get(beanName);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }


    private void creatAsp(Object configBean, Method[] methods) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Aspect.class)) {
                Aspect annotation = method.getAnnotation(Aspect.class);
                String[] targetNames = annotation.Target();
                for (String targetName : targetNames) {
                    Method targetMethod = null;
                    for (Method mm : methods) {
                        if (mm.isAnnotationPresent(Bean.class)) {
                            String beanName = mm.getAnnotation(Bean.class).name();
                            String methodName = mm.getName();
                            if (beanName.equals(targetName) || methodName.equals(targetName)) {
                                targetMethod = mm;
                                break;
                            }
                        }
                    }
                    if (targetMethod == null) {
                        throw new NullPointerException("没有找到指定的切面Bean目标");
                    }

                    Object target = loadMethod(configBean,methods,targetMethod);


                    PointCut aspect = (PointCut) loadMethod(configBean,methods,method);
                    Object proxy = Proxy.newProxyInstance(AnnotationConfigApplicationContext.class.getClassLoader()
                    ,target.getClass().getInterfaces(),aspect);
                    singletonBeanMap.put(targetName, proxy);
                }
            }
        }
    }
    private void loadCofig(Object configBean, Method[] methods) {
        try {

            for (Method method : methods) {
                if (!singletonBeanMap.containsKey(method.getName())) {
                    loadMethod(configBean, methods, method);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Object loadMethod(Object configBean, Method[] methods, Method method) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object bean = null;
        if (method.isAnnotationPresent(Bean.class)) {
            if (singletonBeanMap.containsKey(method.getName()) ||
                    singletonBeanMap.containsKey(method.getAnnotation(Bean.class).name())) {
                bean = singletonBeanMap.get(method.getName());
                if (bean == null) {
                    return singletonBeanMap.get(method.getAnnotation(Bean.class).name());
                } else {
                    return bean;
                }
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            String[] values = method.getAnnotation(Bean.class).value();
            find:for (int i = 0; i < parameterTypes.length; i++) {
                if (values[0].equals("")) {
                    for (Object value : singletonBeanMap.values()) {
                        if (value.getClass() == parameterTypes[i]) {
                            if (parameters[i] != null) {
                                throw new ClassFormatError(parameters[i].getClass() + ":ClassType重复");
                            }
                            parameters[i] = value;
                            continue find;
                        }
                    }
                    for (int i1 = 0; i1 < methods.length; i1++) {
                        if (methods[i1].getReturnType() == parameterTypes[i]) {
                            Object temp = loadMethod(configBean, methods, methods[i1]);
                            if (parameters[i] != null && parameters[i] != temp) {
                                throw new ClassFormatError("ClassType重复");
                            }
                            parameters[i] = temp;
                            continue find;
                        }
                    }
                } else if (singletonBeanMap.containsKey(values[i])) {
                    if (parameterTypes[i] == singletonBeanMap.get(values[i]).getClass()) {
                        parameters[i] = singletonBeanMap.get(values[i]);
                        continue find;
                    }
                }
                for (int i1 = 0; i1 < methods.length; i1++) {
                    if (!methods[i1].isAnnotationPresent(Bean.class)) {
                        break;
                    }
                    String methodName = methods[i1].getAnnotation(Bean.class).name();
                    if (methodName.equals("")) {
                        methodName = methods[i1].getName();
                    }
                    for (String beanName : values){
                        if (methodName.equals(beanName)){
                            parameters[i] = loadMethod(configBean, methods, methods[i1]);
                            break;
                        }
                    }

                }
                if (parameters[i] == null) {
                    throw new NullPointerException(method.getName()+":"+parameterTypes[i].getName()+"没有找到指定的Bean");
                }
            }

//        }
//
//
//            if (values[0].equals("")) {
//                for (int i = 0; i < parameterTypes.length; i++) {
//                    for (Object value : singletonBeanMap.values()) {
//                        if (value.getClass() == parameterTypes[i]) {
//                            if (parameters[i] != null) {
//                                throw new ClassFormatError(parameters[i].getClass() + ":ClassType重复");
//                            }
//                            parameters[i] = value;
//                        }
//                    }
//                    for (int i1 = 0; i1 < methods.length; i1++) {
//                        if (methods[i1].getReturnType() == parameterTypes[i]) {
//                            Object temp = loadMethod(configBean, methods, methods[i1]);
//                            if (parameters[i] != null && parameters[i] != temp) {
//                                throw new ClassFormatError("ClassType重复");
//                            }
//                            parameters[i] = temp;
//                        }
//                    }
//                    if (parameters[i] == null) {
//                        throw new NullPointerException(method.getName() + ":" + parameterTypes[i] + ":没有找到指定的Bean");
//                    }
//                }
//            } else {
//                for (int i = 0; i < parameters.length; i++) {
//                    for (String value : values) {
//                        if (singletonBeanMap.containsKey(value)) {
//                            if (parameterTypes[i] == singletonBeanMap.get(value).getClass()) {
//                                parameters[i] = singletonBeanMap.get(value);
//                                break;
//                            }
//                        } else {
//                            for (int i1 = 0; i1 < methods.length; i1++) {
//                                if (!methods[i1].isAnnotationPresent(Bean.class)) {
//                                    continue;
//                                }
//                                String methodName = methods[i1].getAnnotation(Bean.class).name();
//                                if (methodName.equals("")) {
//                                    methodName = methods[i1].getName();
//                                }
//                                if (methodName.equals(value)) {
//                                    parameters[i] = loadMethod(configBean, methods, methods[i1]);
//                                    break;
//                                }
//                            }
//                        }
//
//                    }
//                    if (parameters[i] == null) {
//                        throw new NullPointerException(parameters[i]+":没有找到指定的Bean");
//                    }
//                }
//            }
            bean = method.invoke(configBean, parameters);
            String beanName = null;
            if (!method.getAnnotation(Bean.class).name().equals("")) {
                beanName = method.getAnnotation(Bean.class).name();
            } else {
                beanName = method.getName();
            }
            if (method.isAnnotationPresent(TranceManage.class)) {
                String dataSourceName = method.getAnnotation(TranceManage.class).dataSource();
                DataSource dataSource = null;
                if (singletonBeanMap.containsKey(dataSourceName)) {
                    dataSource = (DataSource) singletonBeanMap.get(dataSourceName);
                }else {
                    for (int i1 = 0; i1 < methods.length; i1++) {
                        if (!methods[i1].isAnnotationPresent(Bean.class)) {
                            break;
                        }
                        String methodName = methods[i1].getAnnotation(Bean.class).name();
                        if (methodName.equals("")) {
                            methodName = methods[i1].getName();
                        }
                        if (methodName.equals(dataSourceName)){
                            dataSource = (DataSource) loadMethod(configBean, methods, methods[i1]);
                            break;
                        }
                    }
                }
                if (dataSource == null) {
                    throw new NullPointerException(method.getName()+":没有找到指定的dataSource");
                }

                bean = TransactionProxy.createProxy(bean,new TransManagerImpl(dataSource));
            }
            singletonBeanMap.put(beanName, bean);
        }
        return bean;
    }

    private void scanConfig(Class configClass) {
        ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String configPath = componentScan.value();
        configPath = configPath.replace(".", "/");
        rootPath = configPath.substring(0, configPath.indexOf('/'));
        String filePath = AnnotationConfigApplicationContext.class.getClassLoader().getResource(configPath).getFile();
        File configfile = new File(filePath);
        if (configfile.isDirectory()) {
            mkBeanDefinition(configfile);
        }
    }

    private Object creadBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            bean = beanDefinition.getType().getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return bean;
    }

    private void mkBeanDefinition(File file) {

        File[] files = file.listFiles();
        for (File ff : files) {
            if (ff.isDirectory()) {
                mkBeanDefinition(ff);
            } else if (ff.isFile()) {
                if (ff.getName().endsWith(".class")) {
                    String className = ff.getAbsolutePath();
                    className = className.substring(className.indexOf(rootPath), className.indexOf(".class")).replace("\\", ".");
                    Class clazz = null;
                    try {
                        clazz = AnnotationConfigApplicationContext.class.getClassLoader().loadClass(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (clazz.isInterface()) {
                        continue;
                    }
                    if (clazz.isAnnotationPresent(Component.class)) {
                        Component component = (Component) clazz.getAnnotation(Component.class);
                        String beanName = component.value();
                        if (beanName.equals("")){
                            String name = clazz.getName();
                            name = name.substring(name.lastIndexOf(".")+1);
                            char[] chars = name.toCharArray();
                            chars[0]=Character.toLowerCase(chars[0]);
                            beanName = new String(chars);
                        }
                        BeanDefinition beanDefinition = new BeanDefinition();
                        if (clazz.isAnnotationPresent(Scope.class)) {
                            Scope scope = (Scope) clazz.getAnnotation(Scope.class);
                            beanDefinition.setScope(scope.value());
                        } else {
                            beanDefinition.setScope(Scope.SINGLETON);
                        }
                        beanDefinition.setType(clazz);
                        beanDefinitionMap.put(beanName, beanDefinition);
                    }
                }
            }
        }
    }

    @Override
    public Object getBean(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            Object bean = singletonBeanMap.get(name);
            if (bean == null) {
                throw new NullPointerException();
            }
            return bean;
        } else {
            if (beanDefinition.getScope().equals(Scope.SINGLETON)) {
                Object bean = singletonBeanMap.get(name);
                if (bean == null) {
                    bean = creadBean(name, beanDefinition);
                    singletonBeanMap.put(name, bean);
                }
                return bean;
            } else {
                return creadBean(name, beanDefinition);
            }
        }
    }


    @Override
    public boolean containsBean(String name) {
        return singletonBeanMap.containsKey(name);
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
//        Object bean = null;
//        for (Object value : singletonBeanMap.values()) {
//            if (BeanUtil.isExtends(value.getClass(),requiredType)){
//                bean = value;
//                break;
//            }
//        }
//        return (T) bean;
        return null;
    }


    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return (T) getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return null;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit) {
        return null;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) {
        return false;
    }

    @Override
    public Object[] getBeans() {
        return singletonBeanMap.values().toArray(new Object[0]);
    }
}
