package kamo.cloud;

import java.io.Serializable;

public class URL implements Serializable {

    private String protocol;
    private String hostname;
    private Integer port;

    private String version;
    private String interfaceName;
    private Class implClass;


    public URL(String protocol, String hostname, Integer port, String version, String interfaceName, Class implClass) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
        this.version = version;
        this.interfaceName = interfaceName;
        this.implClass = implClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class getImplClass() {
        return implClass;
    }

    public void setImplClass(Class implClass) {
        this.implClass = implClass;
    }
}
