package com.kamo.jdbc.mapper_support;



import com.kamo.jdbc.RowMapper;

import java.util.function.Function;


/**
 * 描述一条Sql的类
 */
public class SqlStatement {
    /**
     * sql语句
     */
    private Function<Object[],String> sqlFunction;

    private String sql;
    /**
     * 是否是查询方法
     */
    private Boolean isQuery;
    //是否是默认的返回类型，查询默认返回List,更新默认返回int
    private Boolean isDefaultReturnType;
    //如果是查询方法代表List的泛型类型
    private Class returnType;

    private boolean isDynamic;
    //映射
    private RowMapper rowMapper;

    public SqlStatement(String sql) {
        this.sql = sql;
        this.isDynamic = false;
    }
    public SqlStatement(Function<Object[],String> sqlFunction) {
        this.sqlFunction = sqlFunction;
        this.isDynamic = true;
    }
    public String getSql() {
        return sqlFunction.apply(null);
    }



    public boolean isQuery() {
        return isQuery;
    }

    public boolean isDefaultReturnType() {
        return isDefaultReturnType;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
    public void setSqlFunction(Function<Object[],String> sqlFunction) {
        this.sqlFunction = sqlFunction;
    }
    public void setQuery(boolean query) {
        isQuery = query;
    }

    public void setDefaultReturnType(boolean defaultReturnType) {
        isDefaultReturnType = defaultReturnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public void setRowMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    public String getDynamicSql(Object...params) {
        return sqlFunction.apply(params);
    }

    public boolean isFirstDynamic(){
        return this.isQuery == null && this.isDefaultReturnType == null;
    }

    public RowMapper getRowMapper() {
        return rowMapper;
    }
}
