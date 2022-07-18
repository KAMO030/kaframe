package com.kamo.jdbc.basedao;

import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.JDBCTemplate;
import com.kamo.jdbc.RowMapper;
import com.kamo.util.BeanUtil;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BaseDao的方法拦截处理器
 */
public class BaseDaoHandler implements InvocationHandler {
    private DataSource dataSource;

    private Map<Method, SqlStatement> sqlStatements;
    private JDBCTemplate jdbcTemplate;
    private BaseDaoImp baseDao;


    public <T> BaseDaoHandler(Map<Method, SqlStatement> sqlStatementMap, Class<T> entityClass, DataSource dataSource) {
        this.sqlStatements = sqlStatementMap;
        this.dataSource = dataSource;
        jdbcTemplate = new JDBCTemplate(dataSource);
        baseDao = new BaseDaoImp(entityClass);

    }

    //代理对象的拦截方法,调用所有的方法都会被此方法拦截,进入此方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            //如果调用的方法是BaseDao里面已经实现的对于单表的CURD则直接调用
            //直接invoke到已经通过子类接口获得的父类BaseDao上的泛型类型(表的实体类型)实例化的BaseDaoImp上
            result = method.invoke(baseDao, args);
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
    private Object invoke(Method method, Object[] args) {
        //通过当前正在执行的方法对象从map缓存中get到具体的sqlStatement对象
        SqlStatement sqlStatement = sqlStatements.get(method);
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
        int argIndex = 0;
        List params = new ArrayList();
        while (sql.matches("(.*)\\$\\{(.*)\\}(.*)")) {
            argIndex = argIndex > args.length - 1 ? args.length - 1 : argIndex++;
            int stratIndex = sql.indexOf("${") + 2;
            int endIndex = sql.indexOf("}", stratIndex);
            String $auto = sql.substring(stratIndex, endIndex);
            $auto = BeanUtil.autoStitchingSql(args[argIndex++], $auto, params);
            sql = sql.substring(0, stratIndex - 2) + $auto + sql.substring(endIndex + 1);
        }
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
        while (sql.matches("(.*)\\$\\{(.*)\\}(.*)")) {
            argIndex = argIndex > args.length - 1 ? args.length - 1 : argIndex++;
            int stratIndex = sql.indexOf("${") + 2;
            int endIndex = sql.indexOf("}", stratIndex);
            String $auto = sql.substring(stratIndex, endIndex);
            $auto = BeanUtil.autoStitchingSql(args[argIndex++], $auto, params);
            sql = sql.substring(0, stratIndex - 2) + $auto + sql.substring(endIndex + 1);
        }
        RowMapper rowMapper = sqlStatement.getRowMapper();
        return sqlStatement.isDefaultReturnType() ?
                jdbcTemplate.query(sql, rowMapper, params.toArray()) :
                jdbcTemplate.queryForObject(sql, rowMapper, params.toArray());
    }

}
