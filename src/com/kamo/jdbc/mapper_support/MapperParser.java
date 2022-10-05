package com.kamo.jdbc.mapper_support;

import com.kamo.core.util.ClassUtils;
import com.kamo.core.util.ReflectUtils;
import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.ColumnMapRowMapper;
import com.kamo.jdbc.RowMapper;
import com.kamo.jdbc.SingleColumnRowMapper;
import com.kamo.jdbc.mapper_support.annotation.SQL;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MapperParser {
    private static final Map<String, Properties> PROPERTY_MAP = new ConcurrentHashMap<>();

    public static Map<Method, SqlStatement> parse(Class daoClass) {
        Map<Method, SqlStatement> sqlStatements = new ConcurrentHashMap<>();
        Method[] methods = daoClass.getMethods();
        String className = daoClass.getName();
        for (Method method : methods) {
            if (method.isAnnotationPresent(SQL.class)) {
                try {
                    sqlStatements.put(method, doParse(method, className));
                } catch (Exception e) {
                    sqlStatements.put(method, doParseDynamic(method, daoClass));
                }

            }
        }
        return sqlStatements;
    }

    private static SqlStatement doParseDynamic(Method method, Class mapperClass) {
        SQL sqlAnno = method.getAnnotation(SQL.class);

        String dynamicSqlStaticMethodName = sqlAnno.dynamicSqlStaticMethodName();
        String staticMethodName = dynamicSqlStaticMethodName.equals("") ? method.getName() + "Sql" : dynamicSqlStaticMethodName;

        List<Class> methodTypes = new ArrayList<>(Arrays.asList(method.getParameterTypes()));
        methodTypes.add(List.class);
        Class[] staticMethodTypes = methodTypes.toArray(new Class[methodTypes.size()]);

        Class dynamicClass = sqlAnno.dynamicSqlMethodClass();
        if (dynamicClass.equals(SQL.class)) {
            dynamicClass = mapperClass;
        }

        Method staticMethod =  ReflectUtils.getMethod(dynamicClass, staticMethodName, staticMethodTypes);

        if (!isSqlStaticMethod(staticMethod)) {
            throw new IllegalArgumentException(staticMethod.getName() + " : 静态Sql方法返回值异常,只能是String类型");
        }

        Method finalStaticMethod = staticMethod;
        Function<Object[], String> sqlFunction = pars -> (String) ReflectUtils.invokeMethod(finalStaticMethod, (Object) null, pars);

        return new SqlStatement(sqlFunction);

    }

    private static boolean isSqlStaticMethod(Method staticMethod) {
        return staticMethod.getReturnType().equals(String.class);
    }

    private static SqlStatement doParse(Method method, String className) {
        String sql = method.getAnnotation(SQL.class).value();
        if (sql.equals("")) {
            sql = getSqlByFile(method.getName(), className);
        }

        SqlStatement sqlStatement = new SqlStatement(sql);
        doParseBySql(sqlStatement, method, sql);

        return sqlStatement;
    }

    private static String getSqlByFile(String methodName, String className) {
        Properties properties;
        if (PROPERTY_MAP.containsKey(className)) {
            properties = PROPERTY_MAP.get(className);
        } else {
            properties = new Properties();
            try {
                properties.load(ClassUtils.getDefaultClassLoader().getResourceAsStream(className.replace('.', File.separatorChar) + ".properties"));
            } catch (Exception e) {
                throw new RuntimeException("找不到: " + className + " 的方法" + methodName + " 接口的映射Sql文件 ");
            }
            PROPERTY_MAP.put(className, properties);
        }
        String property = properties.getProperty(methodName);
        if (property != null) {
            return property;
        }
        throw new NullPointerException("找不到:" + className + " 的 " + methodName + " 方法的映射Sql");
    }

    public static void doParseBySql(SqlStatement sqlStatement, Method method, String sql) {
        Class<?> returnType = method.getReturnType();
        sql = sql.trim();
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
                    ReflectUtils.isPrimitive(returnType) ?
                            new SingleColumnRowMapper(returnType) :
                            Map.class.isAssignableFrom(returnType) ?
                                    new ColumnMapRowMapper() :
                                    new BeanPropertyRowMapper<>(returnType);
        }
        sqlStatement.setReturnType(returnType);
        sqlStatement.setQuery(isQuery);
        sqlStatement.setRowMapper(rowMapper);
        sqlStatement.setDefaultReturnType(isDefaultReturnType);
    }
}
