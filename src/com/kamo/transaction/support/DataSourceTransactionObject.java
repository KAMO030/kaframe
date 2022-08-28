package com.kamo.transaction.support;

import com.kamo.transaction.TransactionDefinition;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class DataSourceTransactionObject  implements TransactionObject{
    private Connection connection;
    private TransactionDefinition definition;
    private boolean isRollback;

    private Integer count = 0;
    public DataSourceTransactionObject(Connection connection, TransactionDefinition definition, boolean isRollback) {
        this.connection = connection;
        this.definition = definition;
        this.isRollback = isRollback;
    }

    public DataSourceTransactionObject(Connection connection, TransactionDefinition definition) {
        this.connection = connection;
        this.definition = definition;
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public Integer getCount() {
        return count;
    }


    protected Integer setCount(Integer count) {
        return this.count = count;
    }

    protected Integer addCount() {
        return count++;
    }
    @Override
    public TransactionDefinition getTransactionDefinition() {
        return this.definition;
    }

    public void setRollback(boolean rollback) {
        isRollback = rollback;
    }

    @Override
    public boolean isRollback() {
        return this.isRollback;
    }

    @Override
    public void whetherToRollBack(Throwable t) {
        Class<? extends Throwable> tClass = t.getClass();
        List<Class<? extends Throwable>> noRollbackFor = Arrays.asList(definition.getNoRollbackFor());
        List<Class<? extends Throwable>> rollbackFor = Arrays.asList(definition.getRollbackFor());

        if (!noRollbackFor.isEmpty()){
            if (noRollbackFor.contains(tClass)) {
                setRollback(false);
                return;
            }
        }else if (!rollbackFor.isEmpty()){
            if (rollbackFor.contains(tClass)) {
                setRollback(true);
                return;
            }
        }
        setRollback(true);
    }
}
