package kamo.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class GenericBeanDefinition implements BeanDefinition {
    protected Class beanClass;

    protected boolean lazyInit;
    protected String scope;
    protected String destroyMethodName;
    protected String initMethodName;
    private Map<String,Property> propertys = new HashMap<>();
    private Map<String,Arguments> argumentsMap = new HashMap<>();
    @Override
    public Class getBeanClass() {
        return beanClass;
    }

    @Override
    public Object doInstance(Object[] args) throws InvocationTargetException, IllegalAccessException {
        return null;
    }
    @Override
    public void addArguments(String argName,Arguments arg){
        argumentsMap.put(argName, arg);
    }


    @Override
    public Class[] getArgTypes() {
        if (argumentsMap.isEmpty()){
            return null;
        }
        Arguments[] argumentsArray = argumentsMap.values().toArray(new Arguments[0]);
        Class[]argTypes = new Class[argumentsArray.length];
        for (int i = 0; i < argumentsArray.length; i++) {
            argTypes[i] = argumentsArray[i].getType();
        }
        return argTypes;
    }

    @Override
    public String[] getArgNames() {
        if (argumentsMap.isEmpty()) {
            return null;
        }
        return argumentsMap.keySet().toArray(new String[0]);
    }

    @Override
    public boolean proContains(String name){
        return propertys.containsKey(name);
    }

    @Override
    public void setArguments(Parameter[] parameters) {
        for (Parameter parameter : parameters) {
            Arguments arguments = new Arguments(parameter);
            addArguments(arguments.getName(),arguments);
        }
    }

    @Override
    public Arguments getArguments(String name) {
        return argumentsMap.get(name);
    }


    @Override
    public void addProperty(Property property){
        propertys.put(property.getName(),property);
    }
    @Override
    public Property getProperty(String name) {
        return propertys.get(name);
    }

    @Override
    public Property[] getPropertys() {
        return propertys.values().toArray(new Property[0]);
    }

    @Override
    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public boolean isLazyInit() {
        return lazyInit;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton() {
        return this.scope.equalsIgnoreCase(BeanDefinition.SCOPE_SINGLETON);
    }

    @Override
    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    @Override
    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    @Override
    public String getInitMethodName() {
        return initMethodName;
    }



    @Override
    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }
}