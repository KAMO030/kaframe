package com.kamo.jdbc.mapper_support;



import com.kamo.bean.annotation.Autowired;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperSupportFactory {
    //数据源
    @Autowired
    private DataSource mapperSupportDataSource;

    //mapper的缓存Map,以mapper的class对象为键,mapper的实例对象为值
    private Map<Class<? extends MapperSupport>, MapperSupport> mapperSupportMap;

    public MapperSupportFactory(DataSource mapperSupportDataSource) {
        this.mapperSupportDataSource = mapperSupportDataSource;
        mapperSupportMap = new ConcurrentHashMap();
    }

    public MapperSupportFactory() {
        mapperSupportMap = new ConcurrentHashMap();
    }

    public <T> T getMapper(Class<? extends MapperSupport> mapperClass) {
        //先看缓存有没有
        if (mapperSupportMap.containsKey(mapperClass)) {
            //有的话直接从缓存拿
            return (T) mapperSupportMap.get(mapperClass);
        }
        //如果没有缓存则新建一个mapper
        T mapper = createMapper(mapperClass);
        //将新建的mapper存入缓存,并返回
        mapperSupportMap.put(mapperClass, (MapperSupport) mapper);
        return mapper;
    }

    private <T> T createMapper(Class<? extends MapperSupport> mapperClass) {
        //先解析传进来的mapper类型,获得句柄和方法的映射Map
        Map<Method, SqlStatement> sqlStatementMap = MapperParser.parse(mapperClass);
        Class<T> entityClass = (Class) ((ParameterizedType) mapperClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        //通过解析出来的映射和自己的dataSource构建一个Mapper方法拦截处理器
        MapperSupportHandler mapperSupportHandler = new MapperSupportHandler(sqlStatementMap,mapperClass,entityClass, mapperSupportDataSource);
        //通过Basemapper方法拦截处理器构造代理对象
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{mapperClass}, mapperSupportHandler);
    }


}
