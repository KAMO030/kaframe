package com.kamo.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class IzumiDataSource implements DataSource {
    private String driver ;
    private String username;
    private String password;
    private String url;
    private int initialSize = 5;
    private int maxActive = 10;
    private int maxWait = 3000;
    private int usedSize;
    private List<Connection> connectionList;
    private boolean isIsolate = true;
    private static ThreadLocal<Connection> local = new ThreadLocal();

    IzumiDataSource() {
    }

    public IzumiDataSource(String driver, String username, String password, String url,
                           int initialSize, int maxActive, int maxWait) {
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.url = url;
        this.initialSize = initialSize;
        this.maxActive = maxActive;
        this.maxWait = maxWait;
        initialcConnectionList();
    }

    public IzumiDataSource(String driverClassName, String username, String password, String url) {
        this.driver = driverClassName;
        this.username = username;
        this.password = password;
        this.url = url;
        initialcConnectionList();
    }

    public boolean isIsolate() {
        return isIsolate;
    }

    public void setIsolate(boolean isolate) {
        isIsolate = isolate;
    }

    private synchronized void initialcConnectionList() {
        try {
            Class.forName(driver,true,this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionList = new ArrayList<>();
        try {
            for (int i = 0; i < initialSize; i++) {
                connectionList.add(new IzumiConnection(DriverManager.getConnection(url, username, password),this));
                usedSize++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {

        return getConnection(username, password);
    }

    @Override
    public synchronized Connection getConnection(String username, String password) throws SQLException {
        if (isIsolate){
            if (local.get()==null){
                local.set(setConnection(username,password));
            }
            return local.get();
        }else {
            return setConnection(username,password);
        }
    }

    private Connection setConnection(String username, String password) throws SQLException {
        Connection conn = null;
        if (connectionList.size() > 0 ) {
            conn = connectionList.remove(0);
        } else if (connectionList.size() == 0 && usedSize < maxActive) {
            conn = new IzumiConnection(DriverManager.getConnection(url, username, password),this);
            usedSize++;
        } else {
            try {
                Thread.sleep(maxWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (connectionList.size() == 0) {
                throw new SQLException();
            } else {
                conn = connectionList.remove(0);
            }
        }
        return conn;
    }


    public void freeConnection() {
        Connection connection = local.get();
        freeConnection(connection);
    }
    public synchronized void  freeConnection(Connection connection) {
        if (connection==null){
            return;
        }
        connectionList.add(connection);
        if (local.get()!=null){
            local.remove();
        }
    }
    private void freeAllConnection() throws SQLException {
        for (Connection connection : connectionList) {
            connection.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        freeAllConnection();
        super.finalize();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.maxWait = seconds;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.maxWait;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
