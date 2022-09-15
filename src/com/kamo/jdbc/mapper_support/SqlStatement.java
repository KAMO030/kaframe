package com.kamo.jdbc.mapper_support;


import com.kamo.jdbc.RowMapper;

/**
 * 描述一条Sql的类
 */
public class SqlStatement {
    /**
     * sql语句
     */
    private String sql;
    /**
     * 是否是查询方法
     */
    private boolean isQuery;
    //是否是默认的返回类型，查询默认返回List,更新默认返回int
    private boolean isDefaultReturnType;
    //如果是查询方法代表List的泛型类型
    private Class returnType;

    //映射
    private RowMapper rowMapper;

    public SqlStatement(String sql, boolean isQuery, boolean isDefaultReturnType, Class returnType,RowMapper rowMapper) {
        this.sql = sql;
        this.isQuery = isQuery;
        this.isDefaultReturnType = isDefaultReturnType;
        this.returnType = returnType;
        this.rowMapper = rowMapper;

    }

    public String getSql() {
        return sql;
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

    public RowMapper getRowMapper() {
        return rowMapper;
    }
}
