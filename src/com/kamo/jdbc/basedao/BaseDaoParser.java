package com.kamo.jdbc.basedao;

import java.lang.reflect.Method;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseDaoParser  {

    public static Map<Method, SqlStatement> parse(Class daoClass){
        Map<Method, SqlStatement> sqlStatements = new ConcurrentHashMap<>();
        Method[] methods = daoClass.getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(SQL.class)){
                sqlStatements.put(method,doParse(method));
            }
        }
        return sqlStatements;
    }

    private static SqlStatement doParse(Method method) {

       String sql =  method.getAnnotation(SQL.class).value().trim()+" ";
       Class<?> returnType = method.getReturnType();
       boolean isQuery =sql.substring(0,sql.indexOf(' ')).equalsIgnoreCase("select");
       boolean isDefaultReturnType = isQuery?
               List.class.isAssignableFrom(returnType):
               returnType.equals(Integer.class)||returnType.equals(int.class);
       if (isQuery&&isDefaultReturnType) {
           returnType =(Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
       }
       return new SqlStatement(sql,isQuery,isDefaultReturnType,returnType);
    }
}
