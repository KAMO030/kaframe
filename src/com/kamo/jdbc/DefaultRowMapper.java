package com.kamo.jdbc;


import com.kamo.core.util.BeanUtils;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRowMapper<T> implements RowMapper {
    private Class type;
    /**
     * 查询完以后每个字段的类型;
     *key:字段名;val:对应的类型代表的整型
     */
    private Map<String, Integer> metaDataMap;

    private Field[] fields;

    public DefaultRowMapper(Class<T> type) {
        this.type = type;
        fields = type.getDeclaredFields();

    }

    @Override
    public T mapRow(ResultSet resultSet) {
        //根据查询到结果集获得每个字段的类型
        if (metaDataMap == null) {
            try {
                metaDataMap = getMetaDataMap(resultSet.getMetaData());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        T bean = null;
        try {
            bean = (T) type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (String columName : metaDataMap.keySet()) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().equals(BeanUtils.toBeanName(columName))) {
                    fields[i].setAccessible(true);
                    try {
                        if (JDBCType.valueOf(metaDataMap.get(columName)).equals(JDBCType.VARCHAR)) {
                            fields[i].set(bean, resultSet.getString(columName));
                        }else if (JDBCType.valueOf(metaDataMap.get(columName)).equals(JDBCType.NUMERIC)){
                            fields[i].set(bean, resultSet.getInt(columName));
                        }else if (JDBCType.valueOf(metaDataMap.get(columName)).equals(JDBCType.DATE)){
                            fields[i].set(bean, resultSet.getDate(columName));
                        }else {
                            fields[i].set(bean, resultSet.getObject(columName));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        return bean;
    }


    private Map<String, Integer> getMetaDataMap(ResultSetMetaData rsmeta) {
        Map<String, Integer> metaDataMap = new ConcurrentHashMap<>();
        int columnCount = 0;
        try {
            columnCount = rsmeta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                metaDataMap.put(rsmeta.getColumnName(i).toUpperCase(), rsmeta.getColumnType(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metaDataMap;
    }

}