package com.kamo.transaction.support;

import com.kamo.proxy.Advice;
import com.kamo.proxy.Advisor;
import com.kamo.proxy.Pointcut;
import com.kamo.proxy.impl.JdkMethodInvocation;
import com.kamo.transaction.TransactionDefinition;
import com.kamo.transaction.TransactionManager;
import com.kamo.transaction.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class TransactionAdvisor implements Advisor {
    private Class<?> beanClass;
    private Map<Method, TransactionDefinition> transactionDefinitions;
    private Supplier<TransactionManager> managerSupplier;
    private TransactionManager manager ;
    public TransactionAdvisor(Class<?> beanClass, Map<Method, TransactionDefinition> transactionDefinitions, Supplier<TransactionManager> managerSupplier) {
        this.beanClass = beanClass;
        this.transactionDefinitions = transactionDefinitions;
        this.managerSupplier = managerSupplier;
    }

    @Override
    public Advice getAdvice() {
        return new TransactionAdvice();
    }

    @Override
    public Pointcut getPointcut() {
        return new TransactionPointcut();
    }
    private class TransactionAdvice implements Advice{

        @Override
        public Object invoke(JdkMethodInvocation invocation) throws Throwable {
            TransactionManager manager = getTransactionManager();
            Object result = null;
            manager.begin(getTransactionDefinition(invocation.getMethod()));
            try {
                result = invocation.proceed();
            } catch (Throwable t) {
                t.printStackTrace();
                TransactionSynchronizationManager.getCurrentTransactionObject().whetherToRollBack(t);
            } finally {
                manager.cleanupAfterCompletion();
            }
            return result;
        }
    }
    private class TransactionPointcut implements Pointcut{
        @Override
        public boolean classFilter(Class<?> targetClass) {
            return beanClass.equals(targetClass);
        }
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return getTransactionDefinition(method)!=null;
        }
    }

    private TransactionManager getTransactionManager() {
        if (Objects.isNull(manager)) {
            manager = managerSupplier.get();
        }
        return manager;
    }

    private TransactionDefinition getTransactionDefinition(Method method){
        Class<?>[] parameterTypes = method.getParameterTypes();
        String name = method.getName();
        Method[] transactionMethods = transactionDefinitions.keySet().toArray(new Method[0]);
        for (Method transactionMethod : transactionMethods) {
            if (Arrays.equals(parameterTypes,transactionMethod.getParameterTypes())&&transactionMethod.getName().equals(name)) {
                return transactionDefinitions.get(transactionMethod);
            }
        }
        return null;
    }
}
