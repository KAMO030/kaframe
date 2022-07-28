package com.kamo.idal;

import com.kamo.jdbc.RowMapper;
import com.kamo.util.BeanUtil;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultMap<T> implements RowMapper<T> {
    private Map<String, String> resultMap;
    private Map<String, Integer> metaDataMap;
    private Class type;

    public ResultMap(Map<String, String> resultMap, Class<T> type) {
        this.resultMap = resultMap;
        this.type = type;
    }
    public ResultMap( Class<T> type) {
        this.type = type;
        this.resultMap = new HashMap<>();
        Field[] fields = type.getDeclaredFields();
        String name;
        for (Field ff : fields){
            name = ff.getName();
            resultMap.put(BeanUtil.toTableName(name),name);
        }
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        if (metaDataMap==null){
            metaDataMap = getMetaDataMap(resultSet.getMetaData());
        }
        System.out.println(resultMap);
        System.out.println(metaDataMap);
        Field field;
        T bean = null;
        try {
            bean = (T) type.newInstance();
            for (String column : resultMap.keySet()) {
                field = type.getDeclaredField(resultMap.get(column));
                field.setAccessible(true);
                if (JDBCType.valueOf(metaDataMap.get(column)).equals(JDBCType.VARCHAR)) {
                    field.set(bean, resultSet.getString(column));
                } else if (JDBCType.valueOf(metaDataMap.get(column)).equals(JDBCType.INTEGER)) {
                    field.set(bean, resultSet.getInt(column));
                } else if (JDBCType.valueOf(metaDataMap.get(column)).equals(JDBCType.DATE)) {
                    field.set(bean, resultSet.getDate(column));
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
