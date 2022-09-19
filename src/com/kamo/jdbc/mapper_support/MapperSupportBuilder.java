package com.kamo.jdbc.mapper_support;

import com.kamo.context.converter.Converter;
import com.kamo.core.util.AnnotationUtils;
import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.JdbcTemplate;
import com.kamo.jdbc.RowMapper;
import com.kamo.jdbc.mapper_support.annotation.*;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MapperSupportBuilder {


    private Class entityClass;
    private JdbcTemplate jdbcTemplate;
    private NameType nameType;


    public MapperSupportBuilder(Class entityClass, JdbcTemplate jdbcTemplate) {
        initialize(entityClass, null, jdbcTemplate, null);
    }

    public MapperSupportBuilder(Class entityClass, DataSource dataSource) {
        initialize(entityClass, dataSource, null, null);
    }

    public MapperSupportBuilder(Class entityClass, JdbcTemplate jdbcTemplate, NameType nameType) {
        initialize(entityClass, null, jdbcTemplate, nameType);
    }

    private void initialize(Class entityClass, DataSource dataSource, JdbcTemplate jdbcTemplate, NameType nameType) {
        this.entityClass = entityClass;
        this.jdbcTemplate = jdbcTemplate != null ? jdbcTemplate : new JdbcTemplate(dataSource);
        this.nameType = nameType == null ? NameType.SAME : nameType;
    }

    public MapperSupport build() {
        Map<String, String> columnNameMap = new ConcurrentHashMap<>();
        String tableName = entityClass.getSimpleName();
        if (entityClass.isAnnotationPresent(TableName.class)) {
            TableName annTable = ((TableName) entityClass.getAnnotation(TableName.class));
            nameType = annTable.nameType();
            String value = annTable.value();
            tableName = value.equals("") ? tableName : value;
        }
        Converter nameConverter = nameType.getConverter();
        tableName = (String) nameConverter.convert(tableName);
        Field[] entityFields = entityClass.getDeclaredFields();
        //默认实体类的第一个字段属性为主键
        Field tableIdField = entityFields[0];
        tableIdField.setAccessible(true);
        TableField tableIdFieldAnn = AnnotationUtils.getAnnotation(tableIdField, TableField.class);
        String fieldIdName;
        if (tableIdFieldAnn != null) {
            String idName = tableIdFieldAnn.value();
            fieldIdName = idName.equals("") ? tableIdField.getName() : idName;
        } else {
            fieldIdName = tableIdField.getName();
        }
        String tableIdName = (String) nameConverter.convert(fieldIdName);
        //将除主键以外其他的属性字段压入map中
        for (int i = 0; i < entityFields.length; i++) {
            entityFields[i].setAccessible(true);
            String fieldName = entityFields[i].getName();
            String columnName = (String) nameConverter.convert(fieldName);
            TableField tableFieldAnn = AnnotationUtils.getAnnotation(entityFields[i], TableField.class);
            if (tableFieldAnn != null) {
                if (!tableFieldAnn.exist()) {
                    continue;
                }
                String tableField = tableFieldAnn.value();
                if (!tableField.equals("")) {
                    columnName = tableField;
                }
            }
            if (entityFields[i].isAnnotationPresent(TableId.class)) {
                Field tempField = entityFields[i];
                entityFields[i] = tableIdField;
                tableIdField = tempField;

                tableIdName = columnName;

                fieldIdName = fieldName;

                columnNameMap.put(tableIdName, fieldIdName);
                continue;
            }
            if (tableIdName.equals(columnName)) {
                continue;
            }
            columnNameMap.put(columnName, fieldName);
        }

        IdType idType = tableIdField.isAnnotationPresent(TableId.class) ?
                tableIdField.getAnnotation(TableId.class).type() :
                IdType.ASSIGN_UUID;
        IdTypeStrategy strategy = idType.getStrategy();
        Objects.requireNonNull(strategy, idType.name() + " :该功能还未实现!");
        Map<String, String> rowMapping = new HashMap(columnNameMap);
        columnNameMap.remove(tableIdName, fieldIdName);
        RowMapper rowMapper = new BeanPropertyRowMapper<>(entityClass, rowMapping);
        return new MapperSupportImpl<>(jdbcTemplate, entityClass, tableName, tableIdField, tableIdName, columnNameMap, rowMapper, strategy);
    }

}
