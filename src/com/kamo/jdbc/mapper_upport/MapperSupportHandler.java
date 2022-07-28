package com.kamo.jdbc.mapper_upport;


import com.kamo.jdbc.JdbcTemplate;
import com.kamo.jdbc.RowMapper;
import com.kamo.util.BeanUtil;
import com.kamo.util.ReflectUtils;

import javax.sql.DataSource;
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
    private DataSource dataSource;

    private Map<Method, SqlStatement> sqlStatements;
    private JdbcTemplate jdbcTemplate;
    private MapperSupport mapperSupport;
    private Class mapperClass;

    public MapperSupportHandler(Map<Method, SqlStatement> sqlStatementMap, Class mapperClass, Class entityClass, DataSource dataSource) {
        this.sqlStatements = sqlStatementMap;
        this.dataSource = dataSource;
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
            //如果调用的方法是BaseDao里面已经实现的对于单表的CURD则直接调用
            //直接invoke到已经通过子类接口获得的父类BaseDao上的泛型类型(表的实体类型)实例化的BaseDaoImp上
            result = method.invoke(mapperSupport, args);
        } catch (IllegalArgumentException e) {
            //如果调用的方法不是在BaseDao里定义的方法,而是在子类接口中定义的方法则会抛出异常
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
        int argIndex = 0;
        List params = new ArrayList();
        sql =  autoStitchingSql(sql , params,args);
        RowMapper rowMapper = sqlStatement.getRowMapper();

        System.out.println("SQL : "+sql);
        System.out.println("PARAMS : "+Arrays.toString(params.toArray()));
        List resultList = jdbcTemplate.query(sql, rowMapper, params.toArray());
        return sqlStatement.isDefaultReturnType() ?
                resultList :
                resultList.isEmpty() ?
                        null :
                        resultList.get(0);
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
                    $auto = BeanUtil.autoStitchingSql(arg, $auto, params);
                    sql = sql.substring(0, stratIndex - 2) + $auto + sql.substring(endIndex + 1);
                }
            }
        }
        return sql;
    }

}
