package com.kamo.transaction;

import com.kamo.transaction.annotation.Isolation;
import com.kamo.transaction.annotation.Propagation;

public class TransactionDefinition {
    private String transactionName;
    private String managerName;
    private Propagation propagation;
    private Isolation isolation;
    private boolean isReadOnly;
    private int timeout;
    Class<? extends Throwable>[] rollbackFor;

    Class<? extends Throwable>[] noRollbackFor;

    public TransactionDefinition(String transactionName,String managerName,
                                 Propagation propagation, Isolation isolation,
                                 boolean isReadOnly, int timeout,
                                 Class<? extends Throwable>[] rollbackFor, Class<? extends Throwable>[] noRollbackFor) {
        this.transactionName = transactionName;
        this.managerName = managerName;
        this.propagation = propagation;
        this.isolation = isolation;
        this.isReadOnly = isReadOnly;
        this.timeout = timeout;
        this.rollbackFor = rollbackFor;
        this.noRollbackFor = noRollbackFor;
    }

    public TransactionDefinition(String transactionName) {
        this.transactionName = transactionName;
        this.managerName = "dataSourceTransManager";
        this.propagation = Propagation.REQUIRED;
        this.isolation = Isolation.DEFAULT;
        this.isReadOnly = false;
        this.timeout = -1;
        this.rollbackFor = new Class[0];
        this.noRollbackFor = new Class[0];
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public Isolation getIsolation() {
        return isolation;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public int getTimeout() {
        return timeout;
    }

    public Class<? extends Throwable>[] getRollbackFor() {
        return rollbackFor;
    }

    public Class<? extends Throwable>[] getNoRollbackFor() {
        return noRollbackFor;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getTransactionName() {
        return transactionName;
    }
}
