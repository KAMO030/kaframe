package com.kamo.transaction;


import com.kamo.transaction.support.TransactionObject;

import java.util.*;

public class TransactionSynchronizationManager {
    private static final ThreadLocal<Map<Object, Object>> resources = new ThreadLocal();
    private static final ThreadLocal<String> currentTransactionName = new ThreadLocal();
    private static final ThreadLocal<Boolean> currentTransactionReadOnly = new ThreadLocal();
    private static final ThreadLocal<Integer> currentTransactionIsolationLevel = new ThreadLocal();
    private static final ThreadLocal<Boolean> actualTransactionActive = new ThreadLocal();

    private static final ThreadLocal<LinkedList<TransactionObject>> transactionObjs = new ThreadLocal();
    public static Object getResource(Object key) {
        return doGetResource(key);
    }
    private static Object doGetResource(Object actualKey) {
        Map<Object, Object> map = (Map)resources.get();
        if (map == null) {
            return null;
        } else {
            Object value = map.get(actualKey);
            return value;
        }
    }
    public static String getCurrentTransactionName(){
        return currentTransactionName.get();
    }
    public static Boolean getCurrentTransactionReadOnly(){
        return currentTransactionReadOnly.get();
    }
    public static Integer getCurrentTransactionIsolationLevel(){
        return currentTransactionIsolationLevel.get();
    }

    public static void setCurrentTransactionName(String name){
        currentTransactionName.set(name);
    }
    public static void setCurrentTransactionReadOnly(Boolean isReadOnly){
        currentTransactionReadOnly.set(isReadOnly);
    }
    public static void setCurrentTransactionIsolationLevel(Integer isolationLevel){
        currentTransactionIsolationLevel.set(isolationLevel);
    }
    public static boolean isSynchronizationActive() {
        Boolean isTransactionActive = actualTransactionActive.get();

        return isTransactionActive==null?
                false:
                isTransactionActive;
    }
    public static void setSynchronizationActive(Boolean isTransactionActive) {
        actualTransactionActive.set(isTransactionActive) ;
    }
    public static void bindResource(Object key, Object value) {
        Objects.requireNonNull(value, "值不能为空");
        Map map = resources.get();
        if (map == null) {
            map = new HashMap();
            resources.set(map);
        }
        Object oldValue = map.put(key, value);
        if (oldValue != null) {
            throw new IllegalStateException("已有值为 [" + oldValue + "] 键为 [" + key + "] 绑定到线程 [" + Thread.currentThread().getName() + "] 上了");
        }
    }

    public static TransactionObject getCurrentTransactionObject() {
        LinkedList<TransactionObject> transactionObjects = transactionObjs.get();
        return transactionObjects ==null|| transactionObjects.isEmpty() ? null : transactionObjects.getLast();
    }
    public static TransactionObject popTransactionObject() {
        LinkedList<TransactionObject> transactionObjects = transactionObjs.get();
        return transactionObjects ==null|| transactionObjects.isEmpty() ?
                null :
                transactionObjects.removeLast();
    }
    public static void addTransactionObject(TransactionObject object) {
        LinkedList<TransactionObject> transactionObjects = transactionObjs.get();
        if (transactionObjects ==null){
            transactionObjects = new LinkedList<>();
            transactionObjs.set(transactionObjects);
        }
        transactionObjects.add(object);
    }

    public static void unBindResource(Object key) {
        resources.get().remove(key);
    }
}
