package com.kamo.datasource;

import java.util.Properties;

public abstract class IzumiDataSourceFactory {
    private IzumiDataSourceFactory() {
    }

    public static IzumiDataSource createDataSource(Properties prop) {

        String driverClassName = prop.getProperty("driverClassName");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String url = prop.getProperty("url");
        int initialSize;
        int maxActive;
        int maxWait;
        try {
            initialSize = Integer.parseInt(prop.getProperty("initialSize"));
            maxActive = Integer.parseInt(prop.getProperty("maxActive"));
            maxWait = Integer.parseInt(prop.getProperty("maxWait"));
        }catch (NumberFormatException e){
            return new IzumiDataSource(driverClassName,username,password,url);
        }
        return new IzumiDataSource(driverClassName,username,password,url,initialSize,maxActive,maxWait);
    }


}
