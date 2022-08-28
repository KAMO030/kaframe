package com.kamo.transaction.support;

import com.kamo.context.annotation.Autowired;
import com.kamo.jdbc.DataSourceUtils;
import com.kamo.transaction.TransactionDefinition;
import com.kamo.transaction.TransactionException;
import com.kamo.transaction.TransactionManager;
import com.kamo.transaction.TransactionSynchronizationManager;
import com.kamo.transaction.annotation.Propagation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;


/**
 * 事务管理的实现类
 */
public class DataSourceTransManager implements TransactionManager {
    @Autowired
    private DataSource dataSourceTransManagerDataSource;

    public DataSourceTransManager(DataSource dataSource) {
        this.dataSourceTransManagerDataSource = dataSource;
    }

    @Override
    public void begin(TransactionDefinition newDefinition) throws SQLException {

        DataSourceTransactionObject transactionObject = (DataSourceTransactionObject) TransactionSynchronizationManager.getCurrentTransactionObject();
        Propagation propagation = newDefinition.getPropagation();
        //如果是第一次就还开启事务或者传播性为需要新的事务
        if (Objects.isNull(transactionObject)|| propagation.equals(Propagation.REQUIRES_NEW)) {
            Connection conn;
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                conn = dataSourceTransManagerDataSource.getConnection();
                TransactionSynchronizationManager.unBindResource(dataSourceTransManagerDataSource);
                TransactionSynchronizationManager.bindResource(dataSourceTransManagerDataSource,conn);
            }else {
                TransactionSynchronizationManager.setSynchronizationActive(true);
                conn = DataSourceUtils.getConnection(dataSourceTransManagerDataSource);
            }
            System.out.println("开始事务");
            transactionObject = new DataSourceTransactionObject(conn,newDefinition);
            TransactionSynchronizationManager.addTransactionObject(transactionObject);
        }else if (propagation.equals(Propagation.NOT_SUPPORTED)){
            String transactionName = transactionObject.getTransactionDefinition().getTransactionName();
            throw new TransactionException(transactionName+" 事务中调用了不支持事务传播的 "+newDefinition.getTransactionName()+" 事务");
        }else {
            transactionObject.addCount();
        }
        Connection con = transactionObject.getConnection();
        TransactionDefinition oldDefinition = transactionObject.getTransactionDefinition();
        Integer isolation = oldDefinition.getIsolation().value();
        Boolean isReadOnly = oldDefinition.isReadOnly();
        Integer currentIsolationLevel = con.getTransactionIsolation();
        if (isolation!=-1&&currentIsolationLevel!=isolation) {
            currentIsolationLevel = isolation;
            con.setTransactionIsolation(isolation);
        }
        con.setReadOnly(isReadOnly);
        con.setAutoCommit(false);
        TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(currentIsolationLevel);
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(isReadOnly);
        TransactionSynchronizationManager.setCurrentTransactionName(oldDefinition.getTransactionName());
    }

    @Override
    public void resume() throws SQLException {
        TransactionObject oldTransactionObject = TransactionSynchronizationManager.getCurrentTransactionObject();
        if (oldTransactionObject == null) {
            TransactionSynchronizationManager.setSynchronizationActive(false);
            return;
        }
        Connection connection = oldTransactionObject.getConnection();
        TransactionDefinition transactionDefinition = oldTransactionObject.getTransactionDefinition();
        Integer isolation = transactionDefinition.getIsolation().value();
        Boolean isReadOnly = transactionDefinition.isReadOnly();
        String transactionName = transactionDefinition.getTransactionName();
        Integer currentIsolationLevel = connection.getTransactionIsolation();
        if (isolation!=-1&&currentIsolationLevel!=isolation) {
            currentIsolationLevel = isolation;
            connection.setTransactionIsolation(isolation);
        }
        TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(currentIsolationLevel);
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(isReadOnly);
        TransactionSynchronizationManager.setCurrentTransactionName(transactionName);
        TransactionSynchronizationManager.bindResource(dataSourceTransManagerDataSource,connection);
    }

    public void doCleanupAfterCompletion() throws SQLException {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject)
                TransactionSynchronizationManager.getCurrentTransactionObject();
        if (txObject.getCount()!=0){
            txObject.setCount(txObject.getCount()-1);
            return;
        }else {
            txObject = (DataSourceTransactionObject) TransactionSynchronizationManager.popTransactionObject();
        }
        Connection con = txObject.getConnection();
        Objects.requireNonNull(con);
        if (txObject.isRollback()) {
            System.out.println("回滚事务");
            con.rollback();
        }else if (!con.isReadOnly()){
            System.out.println("提交事务");
            con.commit();
        }
        con.setAutoCommit(true);
        DataSourceUtils.releaseConnection(dataSourceTransManagerDataSource,con);
        resume();
    }

    public void cleanupAfterCompletion()  {
        try {
            doCleanupAfterCompletion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
