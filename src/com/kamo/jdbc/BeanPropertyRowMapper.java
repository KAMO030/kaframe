package com.kamo.jdbc;

import com.kamo.jdbc.mapper_support.annotation.NameType;
import com.kamo.jdbc.mapper_support.annotation.TableField;
import com.kamo.jdbc.mapper_support.annotation.TableName;
import com.kamo.util.BeanUtil;
import com.kamo.context.converter.Converter;
import com.kamo.util.AnnotationUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BeanPropertyRowMapper<T> implements RowMapper<T> {

    private Class type;
    /**
     * 查询完以后每个字段的类型;
     * key:字段名;val:字段名对应的Field对象
     */
    private Map<String, String> propertyMapping;


    public BeanPropertyRowMapper(Class<T> type) {
        this.type = type;
        initializerMapping();
    }

    public BeanPropertyRowMapper(Class type, Map<String, String> propertyMapping) {
        this.type = type;
        this.propertyMapping = propertyMapping;
    }

    protected void initializerMapping() {
        try {
            mappingNameByField();
        } catch (SQLException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public T mapRow(ResultSet resultSet) {
        T entity = null;
        try {
            //首次调用时才会进入if
            if (propertyMapping == null) {
                mappingNameByResultSet(resultSet.getMetaData());
            }
            //如果没有一个字段映射匹配则直接返回null
            if (propertyMapping.isEmpty()) {
                return null;
            }
            entity = (T) type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Map.Entry<String, String> propertyEntry : propertyMapping.entrySet()) {
            String columnName = propertyEntry.getKey();
            String fieldName = propertyEntry.getValue();
            try {
                mapping(entity, resultSet, columnName, fieldName);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    private void mapping(T entity, ResultSet resultSet, String columnName, String fieldName) throws NoSuchFieldException, SQLException, IllegalAccessException {
        Field propertyField = type.getDeclaredField(fieldName);
        propertyField.setAccessible(true);
        try {
            propertyField.set(entity, resultSet.getObject(columnName));
        } catch (IllegalAccessException ex) {
            propertyField.set(entity, resultSet.getObject(BeanUtil.toBeanName(columnName)));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 根据查询到结果集的数据源的每个字段的名字寻找名字相同的field对象
     *
     * @param metaData 查询到结果集的数据源
     * @throws SQLException
     */
    private void mappingNameByResultSet(ResultSetMetaData metaData) throws SQLException {
        propertyMapping = new HashMap<>();
        //获得结果集的列数
        int columnCount = metaData.getColumnCount();
        //遍历结果集的每个字段名,看看能不能在type找到对应的field对象
        for (int i = 0; i < columnCount; i++) {
            String columnName = metaData.getColumnName(i + 1);
            Field field = null;
            try {
                field = type.getDeclaredField(columnName);
            } catch (NoSuchFieldException e) {
                //如果字段名和和属性名不一样,则会找不到抛异常
                try {
                    //通过BeanUtil工具类将字段名转换成一定规则的名字在去找
                    //U_ID->uId  (把字符全转为小写,把下划线去掉并把下划线后的一个字母转为大写)
                    field = type.getDeclaredField(BeanUtil.toBeanName(columnName));
                } catch (NoSuchFieldException ex) {
                    //如果还是找不到,则打印异常
                    //并继续寻找映射
                    ex.printStackTrace();
                    continue;
                }
            }
            propertyMapping.put(columnName, field.getName());
        }
    }

    private void mappingNameByField() throws SQLException {
        propertyMapping = new HashMap<>();
        //获得结果集的列数
        Field[] fields = type.getDeclaredFields();

        TableName tableName = (TableName) type.getAnnotation(TableName.class);
        Converter<String,String> converter = (tableName!=null ? tableName.nameType() : NameType.SAME).getConverter();
        for (Field field : fields) {
            String fieldName = field.getName();
            TableField annotation = AnnotationUtils.getAnnotation(field, TableField.class);
            String columnName = converter.convert(fieldName);
            if (annotation != null && !annotation.value().equals("")) {
                columnName = annotation.value();
            }
            propertyMapping.put(columnName, fieldName);
        }

    }
}
