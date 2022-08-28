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

    private Transactional classTransactional;
    public TransactionAdvisorBuilder(Class<?> beanClass) {
        this.beanClass = beanClass;
        enableMethods = new HashSet<>();
    }

    public  TransactionAdvisor buildAdvisor(Supplier<TransactionManager>managerSupplier) {
        Map<Method,TransactionDefinition> transactionDefinitions = new HashMap<>();

        for (Method method : enableMethods) {
            if (method.isAnnotationPresent(Transactional.class)) {
                transactionDefinitions.put(method, TransactionDefinitionBuild.build(method.getAnnotation(Transactional.class),method));
            }else {
                transactionDefinitions.put(method, TransactionDefinitionBuild.build(classTransactional,method));
            }
        }
        return new TransactionAdvisor(beanClass,transactionDefinitions,managerSupplier);
    }

    public boolean isEnableTransaction() {
        Method[] methods = beanClass.getDeclaredMethods();
        if (beanClass.isAnnotationPresent(Transactional.class)) {
            classTransactional = beanClass.getAnnotation(Transactional.class);
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
