package com.kamo.factory;

import java.util.List;
@Deprecated
class Beans {
    private String id;
    private String className;
    private Object obj;
    private List parameters;
    private String factoryId;
    private String factoryMethodName;
    private String factoryMethodPar;
    private String[] factoryMethodParTypes;
    public Beans() {

    }

    public String[] getFactoryMethodParTypes() {
        return factoryMethodParTypes;
    }

    public void setFactoryMethodParTypes(String[] factoryMethodParTypes) {
        this.factoryMethodParTypes = factoryMethodParTypes;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public String getFactoryMethodPar() {
        return factoryMethodPar;
    }

    public void setFactoryMethodPar(String factoryMethodPar) {
        this.factoryMethodPar = factoryMethodPar;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public void setParameters(List parameters) {
        this.parameters = parameters;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object bean) {
        this.obj = bean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List getParameters() {
        return parameters;
    }


}
