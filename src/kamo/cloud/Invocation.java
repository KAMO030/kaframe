package kamo.cloud;

import java.io.Serializable;
import java.util.Arrays;

public class Invocation implements Serializable {
    private static final long serialVersionUID = -684979470754667710L;
    private String interfaceName;
    private String version;
    private String methodName;
    private Class[] paramTypes;
    private Object[] params;


    public Invocation(String interfaceName, String version, String methodName, Class[] paramTypes, Object[] params) {
        this.interfaceName = interfaceName;
        this.version = version;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.params = params;
    }

    public Invocation() {
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Invocation{" +
                "interfaceName='" + interfaceName + '\'' +
                ", version='" + version + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
