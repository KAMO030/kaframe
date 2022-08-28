package com.kamo.jdbc;

import com.kamo.transaction.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DataSourceUtils {
    public static Connection getConnection(DataSource dataSource) throws CannotGetJdbcConnectionException {
        try {
            return doGetConnection(dataSource);
        } catch (SQLException var2) {
            throw new CannotGetJdbcConnectionException("获取 JDBC 连接失败", var2);
        } catch (IllegalStateException var3) {
            throw new CannotGetJdbcConnectionException("获取 JDBC 连接失败: " + var3.getMessage());
        }
    }

    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        Objects.requireNonNull(dataSource, "不存在数据源");
        Connection connection = (Connection) TransactionSynchronizationManager.getResource(dataSource);
        if (Objects.nonNull(connection)) {
            return connection;
        }
        connection = dataSource.getConnection();
        //是否存在事务
        if (TransactionSynchronizationManager.isSynchronizationActive()){
            //绑定资源
            TransactionSynchronizationManager.bindResource(dataSource, connection);
        }
        return connection;
    }

    public static void releaseConnection(DataSource dataSource, Connection con) throws SQLException {
        Objects.requireNonNull(dataSource, "不存在数据源");
        //是否存在事务
        if (TransactionSynchronizationManager.isSynchronizationActive()){
            //解绑资源
            TransactionSynchronizationManager.unBindResource(dataSource);
        }
        con.close();
    }
    public static void releaseConnection(Connection con)  {
        Objects.requireNonNull(con, "不存在连接");
        if (TransactionSynchronizationManager.isSynchronizationActive()){
            return;
        }
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
