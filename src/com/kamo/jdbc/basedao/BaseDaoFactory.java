package com.kamo.jdbc.basedao;

import com.kamo.context.FactoryBean;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseDaoFactory {
    //数据源
    private DataSource dataSource;

    //dao的缓存Map,以dao的class对象为键,dao的实例对象为值
    private Map<Class<? extends BaseDao>, BaseDao> daoMap;

    public BaseDaoFactory(DataSource dataSource) {
        this.dataSource = dataSource;
        daoMap = new ConcurrentHashMap();
    }

    public <T> T getBaseDao(Class<? extends BaseDao> daoClass) {
        //先看缓存有没有
        if (daoMap.containsKey(daoClass)) {
            //有的话直接从缓存拿
            return (T) daoMap.get(daoClass);
        }
        //如果没有缓存则新建一个dao
        T dao = createDao(daoClass);
        //将新建的dao存入缓存,并返回
        daoMap.put(daoClass, (BaseDao) dao);
        return dao;
    }

    private <T> T createDao(Class<? extends BaseDao> daoClass) {
        //先解析传进来的dao类型,获得句柄和方法的映射Map
        Map<Method, SqlStatement> sqlStatementMap = BaseDaoParser.parse(daoClass);
        Class<T> entityClass = (Class) ((ParameterizedType) daoClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        //通过解析出来的映射和自己的dataSource构建一个BaseDao方法拦截处理器
        BaseDaoHandler baseDaoHandler = new BaseDaoHandler(sqlStatementMap,entityClass, dataSource);
        //通过BaseDao方法拦截处理器构造代理对象
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{daoClass}, baseDaoHandler);
    }


}
