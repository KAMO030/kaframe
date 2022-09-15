package com.kamo.jdbc.mapper_support;

import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.ColumnMapRowMapper;
import com.kamo.jdbc.RowMapper;
import com.kamo.jdbc.SingleColumnRowMapper;
import com.kamo.jdbc.mapper_support.annotation.SQL;
import com.kamo.util.ReflectUtil;
import com.kamo.util.Resource;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MapperParser {
    private static final Map<String, Properties> PROPERTY_MAP = new ConcurrentHashMap<>();

    public static Map<Method, SqlStatement> parse(Class daoClass) {
        Map<Method, SqlStatement> sqlStatements = new ConcurrentHashMap<>();
        Method[] methods = daoClass.getMethods();
        String className = daoClass.getName();
        for (Method method : methods) {
            if (method.isAnnotationPresent(SQL.class)) {
                sqlStatements.put(method, doParse(method, className));
            }
        }
        return sqlStatements;
    }

    private static SqlStatement doParse(Method method, String className) {
        String sql = method.getAnnotation(SQL.class).value();
        if (sql.equals("")) {
            sql = getSqlByFile(method.getName(), className);
        }
        sql = sql.trim();
        Class<?> returnType = method.getReturnType();
        int endIndex = sql.indexOf(' ');
        //获得sql语句的第一个单词看他是不是select
        boolean isQuery = sql.substring(0, endIndex).equalsIgnoreCase("select");
        //如果是查询就看返回值类型是不是List或者List的子类,如果是就是默认返回类型,isDefaultReturnType=ture
        //入如果不是查询看他的返回值是不是int或Integer类型,如果是就是默认返回类型,isDefaultReturnType=ture
        boolean isDefaultReturnType = isQuery ?
                List.class.isAssignableFrom(returnType) :
                returnType.equals(Integer.class) || returnType.equals(int.class);
        //如果是查询sql并且返回值类型是默认返回值类型(List或者List的子类),则把返回值类型class设置成List的泛型类型
        if (isQuery && isDefaultReturnType && !Map.class.isAssignableFrom(returnType)) {
            returnType = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        }
        RowMapper rowMapper = null;
        //如果此sql是查询sql
        if (isQuery) {
            //判断返回类型是不是八大原始型或其包装类,如果是则采用简单映射器,否则才用Bean的属性映射器
            rowMapper =
                    ReflectUtil.isPrimitive(returnType) ?
                            new SingleColumnRowMapper(returnType) :
                            Map.class.isAssignableFrom(returnType) ?
                                    new ColumnMapRowMapper() :
                                    new BeanPropertyRowMapper<>(returnType);
        }
        return new SqlStatement(sql, isQuery, isDefaultReturnType, returnType, rowMapper);
    }

    private static String getSqlByFile(String methodName, String className) {
        Properties properties;
        if (PROPERTY_MAP.containsKey(className)) {
            properties = PROPERTY_MAP.get(className);
        } else {
            properties = new Properties();
            try {
                properties.load(Resource.getResourceAsReader(className.replace('.', '/') + ".properties"));
            } catch (Exception e) {
                throw new RuntimeException("找不到: " + className + " 接口的映射Sql文件 ");
            }
            PROPERTY_MAP.put(className, properties);
        }
        String property = properties.getProperty(methodName);
        if (property != null) {
            return property;
        }
        throw new NullPointerException("找不到:" + className + " 的 " + methodName + " 方法的映射Sql");
    }

}
