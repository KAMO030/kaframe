package com.kamo.jdbc.mapper_support;


import com.kamo.core.util.ReflectUtils;
import com.kamo.jdbc.JdbcTemplate;
import com.kamo.jdbc.RowMapper;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MapperSupport的方法拦截处理器
 */
public class MapperSupportHandler implements InvocationHandler {
    private Map<Method, SqlStatement> sqlStatements;
    private JdbcTemplate jdbcTemplate;
    private MapperSupport mapperSupport;
    private Class mapperClass;

    public MapperSupportHandler(Map<Method, SqlStatement> sqlStatementMap, Class mapperClass, Class entityClass, DataSource dataSource) {
        this.sqlStatements = sqlStatementMap;
        this.mapperClass = mapperClass;
        jdbcTemplate = new JdbcTemplate(dataSource);
        mapperSupport = new MapperSupportBuilder(entityClass,jdbcTemplate).build();

    }

    //代理对象的拦截方法,调用所有的方法都会被此方法拦截,进入此方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString")&&method.getParameterTypes().length == 0){
            return mapperClass.getName()+"$proxy@"+Integer.toHexString(mapperSupport.hashCode());
        }
        Object result = null;
        try {
            //如果调用的方法是MapperSupportImp里面已经实现的对于单表的CURD则直接调用
            //直接invoke到已经通过子类接口获得的父类MapperSupportImp上的泛型类型(表的实体类型)实例化的BaseDaoImp上
            result = method.invoke(mapperSupport, args);
        } catch (IllegalArgumentException e) {
            //如果调用的方法不是在MapperSupportImp里定义的方法,而是在子类接口中定义的方法则会抛出异常
            //则根据sqlStatements缓存去执行sql得到返回值返回
            result = invoke(method, args);
        }
        return result;
    }

    /**
     * 根据sqlStatements缓存去执行sql得到返回值
     * @param method 当前执行的方法
     * @param args 执行方法时传入的实参
     * @return
     */
    private Object invoke(Method method, Object[] args) throws NoSuchMethodException {
        //通过当前正在执行的方法对象从map缓存中get到具体的sqlStatement对象
        SqlStatement sqlStatement = sqlStatements.get(method);
        if (sqlStatement == null) {
            throw new NoSuchMethodException("没有找到: "+mapperClass+" 中 "+method.getName()+" 方法的Sql映射");
        }
        //通过sqlStatement判断此方法是查询还是更新
        return sqlStatement.isQuery() ? doQuery(sqlStatement, args) :
                doUpdate(sqlStatement, args);
    }

    /**
     * 执行更新
     * @param sqlStatement 当前执行的方法对应的sql描述对象
     * @param args 执行方法时传入的实参
     * @return
     */
    private Object doUpdate(SqlStatement sqlStatement, Object[] args) {
        String sql = sqlStatement.getSql();
        List params = new ArrayList();
        sql =  autoStitchingSql(sql , params,args);
        System.out.println("SQL : "+sql);
        System.out.println("PARAMS : "+Arrays.toString(params.toArray()));
        int row = jdbcTemplate.update(sql, params.toArray());
        return sqlStatement.isDefaultReturnType() ?
                row :
                row > 0;
    }
    /**
     * 执行查询
     * @param sqlStatement 当前执行的方法对应的sql描述对象
     * @param args 执行方法时传入的实参
     * @return
     */
    private Object doQuery(SqlStatement sqlStatement, Object[] args) {
        String sql = sqlStatement.getSql();
        List paramsList = new ArrayList();
        sql = autoStitchingSql(sql, paramsList, args);
        RowMapper rowMapper = sqlStatement.getRowMapper();
        return sqlStatement.isDefaultReturnType() ?
                doQueryForList(sql,rowMapper,paramsList) :
                doQueryForObject(sql,rowMapper,paramsList);
    }

    private Object doQueryForObject(String sql,RowMapper rowMapper,List paramsList) {
        Object[] params = paramsList.toArray();
        List resultList = mapperSupport.query(sql, rowMapper, params);
        return resultList.isEmpty() ?
                null : resultList.get(0);
    }

    private <T> List<T> doQueryForList(String sql, RowMapper<T> rowMapper, List paramsList) {
        IPage<T> page = PageHelper.getPage();
        if (page!=null&&page.getCurrentPage()!=null&&page.getPageSize()!=null) {
            int select = sql.indexOf("select");
            int from = sql.indexOf("from");
            Object[] params = paramsList.toArray();
            String countSql = sql.replace(sql.substring(select+7,from-1),"COUNT(*)")  ;
            Integer totalCount = jdbcTemplate.queryForObject(countSql,Integer.class,params);
            page.setTotalCount(totalCount);
            sql+=" limit ?,? ";
            paramsList.add(page.getStartPage());
            paramsList.add(page.getPageSize());
            params = paramsList.toArray();
            List resultList = mapperSupport.query(sql, rowMapper, params);
            page.setDataList(resultList);
            PageHelper.removePage();
            return page;
        }else {
            Object[] params = paramsList.toArray();
            List<T> resultList = mapperSupport.query(sql, rowMapper, params);
            return resultList;
        }
    }
    private String autoStitchingSql(String sql , List params, Object...args){
        if(args==null||args.length == 0){
            return sql;
        }
        for (Object arg : args) {
            if (ReflectUtils.isPrimitive(arg.getClass())) {
                params.add(arg);
            }else {
                if (sql.matches("(.*)\\$\\{(.*)\\}(.*)")){
                    int stratIndex = sql.indexOf("${") + 2;
                    int endIndex = sql.indexOf("}", stratIndex);
                    String $auto = sql.substring(stratIndex, endIndex);
                    $auto = autoStitchingSql(arg, $auto, params);
                    sql = sql.substring(0, stratIndex - 2) + $auto + sql.substring(endIndex + 1);
                }
            }
        }
        return sql;
    }
    private   String autoStitchingSql(Object entity, String refer, List args) {
        String stitchingSql = "";
        if (entity == null) {
            return "";
        }
        Class<?> entityClass = entity.getClass();
        Field[] entityFields = entityClass.getDeclaredFields();
        try {
            for (Field entityField : entityFields) {
                Object value = null;
                String columnName = entityField.getName();
                entityField.setAccessible(true);
                value = entityField.get(entity);
                if (value != null) {
                    stitchingSql += refer.replace("$", columnName);
                    if (refer.indexOf("like") != -1) {
                        value = "%" + value + "%";
                    }
                    args.add(value);
                }
            }
            return stitchingSql;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
