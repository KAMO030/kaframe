package com.kamo.jdbc.mapper_upport;

import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.JdbcTemplate;
import com.kamo.jdbc.RowMapper;
import com.kamo.jdbc.mapper_upport.annotation.FieldName;
import com.kamo.jdbc.mapper_upport.annotation.PrimeField;
import com.kamo.jdbc.mapper_upport.annotation.TableName;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperSupportBuilder {
    private Class entityClass;
    private JdbcTemplate jdbcTemplate;


    public MapperSupportBuilder(Class entityClass, JdbcTemplate jdbcTemplate) {
        this.entityClass = entityClass;
        this.jdbcTemplate = jdbcTemplate;
    }

    public MapperSupport build(){

        String tableName = entityClass.isAnnotationPresent(TableName.class)?
                ((TableName)entityClass.getAnnotation(TableName.class)).value() :
                entityClass.getSimpleName();;
        RowMapper rowMapper = new BeanPropertyRowMapper<>(entityClass);
        Field[] entityFields = entityClass.getDeclaredFields();
        Map<String, Field> columnNameMap = new ConcurrentHashMap<>();
        //默认实体类的第一个字段属性为主键
        Field primaryKeyF = entityFields[0];
        primaryKeyF.setAccessible(true);
        String primaryKey = primaryKeyF.getName();
        //将除主键以外其他的属性字段压入map中
        for (int i = 1; i < entityFields.length; i++) {
            entityFields[i].setAccessible(true);

            String fieldName = entityFields[i].isAnnotationPresent(FieldName.class)?
                    entityFields[i].getAnnotation(FieldName.class).value() :
                    entityFields[i].getName();
            if (entityFields[i].isAnnotationPresent(PrimeField.class)) {
                Field tempField = entityFields[i];
                entityFields[i] = primaryKeyF;
                primaryKeyF = tempField;
                String tempName = primaryKey;
                primaryKey = fieldName;
                fieldName = tempName;
            }
            columnNameMap.put(fieldName, entityFields[i]);
        }
        return new MapperSupportImpl<>(jdbcTemplate,entityClass,tableName,primaryKeyF,primaryKey,columnNameMap,rowMapper);
    }
}
