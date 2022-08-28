package com.kamo.jdbc.basedao;

import com.kamo.util.Resource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
@Deprecated

public class BaseDaoParser  {
    private static final Map<String,Properties>PROPERTY_MAP = new ConcurrentHashMap<>();
    public static Map<Method, SqlStatement> parse(Class daoClass){
        Map<Method, SqlStatement> sqlStatements = new ConcurrentHashMap<>();
        Method[] methods = daoClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(SQL.class)) {
                sqlStatements.put(method,doParse(method,daoClass.getName()));
            }
        }
        return sqlStatements;
    }

    private static SqlStatement doParse(Method method, String className) {
        String sql = method.getAnnotation(SQL.class).value();
       if(sql.equals("")){
            sql =getSqlByFile(method.getName(),className);
        }
        sql =  sql.trim();
       Class<?> returnType = method.getReturnType();
        int endIndex = sql.indexOf(' ');
        //获得sql语句的第一个单词看他是不是select
        boolean isQuery =sql.substring(0, endIndex).equalsIgnoreCase("select");
        //如果是查询就看返回值类型是不是List或者List的子类,如果是就是默认返回类型,isDefaultReturnType=ture
        //入如果不是查询看他的返回值是不是int或Integer类型,如果是就是默认返回类型,isDefaultReturnType=ture
       boolean isDefaultReturnType = isQuery?
               List.class.isAssignableFrom(returnType):
               returnType.equals(Integer.class)||returnType.equals(int.class);
       //如果是查询sql并且返回值类型是默认返回值类型(List或者List的子类),则把返回值类型class设置成List的泛型类型
       if (isQuery&&isDefaultReturnType) {
           returnType =(Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
       }
       return new SqlStatement(sql,isQuery,isDefaultReturnType,returnType);
    }

    private static String getSqlByFile(String methodName, String className) {
        Properties properties;
        if (PROPERTY_MAP.containsKey(className)) {
            properties = PROPERTY_MAP.get(className);
        }else {
            properties  = new Properties();
            try {
                properties.load(Resource.getResourceAsReader(className.replace('.', '/')+ "Mapper.properties"));
            } catch (IOException e) {
                throw new NullPointerException("找不到:"+className+" 接口的映射Sql文件" );
            }
            PROPERTY_MAP.put(className,properties);
        }
        String property = properties.getProperty(methodName);
        if (property != null) {
            return property;
        }
        throw new NullPointerException("找不到:"+className+" 的 "+methodName+" 方法的映射Sql" );
    }
}
