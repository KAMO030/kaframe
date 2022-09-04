package com.kamo.jdbc.basedao;

import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.RowMapper;
import com.kamo.jdbc.SingleColumnRowMapper;
import com.kamo.util.AnnotationUtils;
import com.kamo.util.ReflectUtil;

@Deprecated

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
    private RowMapper mapper;

    public SqlStatement(String sql, boolean isQuery, boolean isDefaultReturnType, Class returnType) {
        this.sql = sql;
        this.isQuery = isQuery;
        this.isDefaultReturnType = isDefaultReturnType;
        this.returnType = returnType;
        //如果此sql是查询sql
        if (this.isQuery) {
            //判断返回类型是不是八大原始型或其包装类,如果是则采用简单映射器,否则才用Bean的属性映射器
            this.mapper = ReflectUtil.isPrimitive(returnType)?
                    new SingleColumnRowMapper(returnType):
                    new BeanPropertyRowMapper<>(returnType);
        }
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

    public RowMapper getRowMapper() {
        return mapper;
    }
}
