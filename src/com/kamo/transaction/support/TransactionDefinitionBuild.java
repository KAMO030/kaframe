package com.kamo.transaction.support;

import com.kamo.transaction.TransactionDefinition;
import com.kamo.transaction.annotation.Isolation;
import com.kamo.transaction.annotation.Propagation;
import com.kamo.transaction.annotation.Transactional;

import java.lang.reflect.Method;

public class TransactionDefinitionBuild {
    public static TransactionDefinition build(Method method) {
        if (!method.isAnnotationPresent(Transactional.class)) {
            return new TransactionDefinition(method.getName());
        }
        Transactional transactionalAnnotation = method.getAnnotation(Transactional.class);
        String transactionManagerName = transactionalAnnotation.value();
        if (transactionManagerName.equals("")) {
            transactionManagerName = method.getName();
        }
        String managerName = transactionalAnnotation.managerName();
        Propagation propagation = transactionalAnnotation.propagation();
        Isolation isolation = transactionalAnnotation.isolation();
        Integer timeout = transactionalAnnotation.timeout();
        Boolean readOnly = transactionalAnnotation.readOnly();
        Class<? extends Throwable>[] rollbackFor = transactionalAnnotation.rollbackFor();
        Class<? extends Throwable>[] noRollbackFor = transactionalAnnotation.noRollbackFor();
        return new TransactionDefinition(transactionManagerName,managerName,
                propagation,isolation,
                readOnly,timeout,
                rollbackFor,noRollbackFor);
    }
}
