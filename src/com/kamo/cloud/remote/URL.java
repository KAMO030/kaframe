package com.kamo.cloud.remote;

import java.io.Serializable;

public class URL implements Serializable {
    private String ipAddress;
    private Integer port;

    public URL(String ipAddress, Integer port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getPort() {
        return port;
    }
}
