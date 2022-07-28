package com.kamo.transaction.support;

import com.kamo.transaction.TransactionDefinition;
import com.kamo.transaction.TransactionManager;
import com.kamo.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class TransactionAdvisorBuilder {
    private Class<?> beanClass;
    private Set<Method> enableMethods;
    public TransactionAdvisorBuilder(Class<?> beanClass) {
        this.beanClass = beanClass;
        enableMethods = new HashSet<>();
    }

    public  TransactionAdvisor buildAdvisor(Supplier<TransactionManager>managerSupplier) {
        Map<Method,TransactionDefinition> transactionDefinitions = new HashMap<>();
        for (Method method : enableMethods) {
            transactionDefinitions.put(method, TransactionDefinitionBuild.build(method));
        }
        return new TransactionAdvisor(beanClass,transactionDefinitions,managerSupplier);
    }

    public boolean isEnableTransaction() {
        Method[] methods = beanClass.getDeclaredMethods();
        if (beanClass.isAnnotationPresent(Transactional.class)) {
            enableMethods.addAll(Arrays.asList(methods));
            return true;
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(Transactional.class)) {
                enableMethods.add(method);
            }
        }
        return !enableMethods.isEmpty();
    }

}
