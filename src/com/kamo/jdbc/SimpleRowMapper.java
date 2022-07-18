package com.kamo.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleRowMapper<T> implements RowMapper<T>{

    public Class<T> type;
    public SimpleRowMapper(Class<T> type) {
        this.type = type;
    }


    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        if(resultSet.getMetaData().getColumnCount()!=1){
            throw new SQLException("");
        }
        Object result = resultSet.getObject(1);
        if (result.getClass().equals(type)) {
            return (T) result;
        }
        try {
            return (T) type.getMethod("valueOf", String.class).invoke(null,result.toString());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
