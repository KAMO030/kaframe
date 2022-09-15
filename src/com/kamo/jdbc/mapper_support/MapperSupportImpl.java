package com.kamo.jdbc.mapper_support;


import com.kamo.jdbc.JdbcTemplate;
import com.kamo.jdbc.RowMapper;
import java.lang.reflect.Field;
import java.util.*;

public class MapperSupportImpl<T> implements MapperSupport<T> {
    protected JdbcTemplate jdbcTemplate;
    //子类dao泛型的类型对应实体类型
    protected Class entityClass;
    //表名
    protected String tableName;
    //主键属性字段对象
    protected Field idField;
    //主键属性字段的名字
    protected String idName;
    //除主键以外其他属性字段的名字与属性字段对象的map
    protected Map<String, String> columnNameMap;
    //jdbcTemp的映射
    protected RowMapper<T> rowMapper;
    protected IdTypeStrategy idTypeStrategy;

    public MapperSupportImpl(JdbcTemplate jdbcTemplate, Class entityClass, String tableName,
                             Field idField, String idName,
                             Map<String, String> columnNameMap, RowMapper<T> rowMapper, IdTypeStrategy idTypeStrategy) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.idField = idField;
        this.idName = idName;
        this.columnNameMap = columnNameMap;
        this.rowMapper = rowMapper;
        this.idTypeStrategy = idTypeStrategy;
    }


    @Override
    public int insert(T entity) {
        List args = new ArrayList();
        String placeHolder = idTypeStrategy.assembly(args, entity, idField);
        //自动拼接表名和主键名
        StringJoiner sql = new StringJoiner(",","INSERT INTO "+tableName+" (",")");
        //自动拼接values的问号占位符，默认有一个问号是主键
        StringJoiner values = new StringJoiner(","," VALUES(",")");
        if (!args.isEmpty()) {
            sql.add(idName);
            values.add(placeHolder);
        }
        //循环遍历该类的每一个属性名字
        for (String columnName : columnNameMap.keySet()) {
            try {
                Field field = entityClass.getDeclaredField(columnNameMap.get(columnName));
                field.setAccessible(true);
                Object value = field.get(entity);
                //如果传入的entity的名为columnName的属性的值不等于null，则会自动拼接Sql,和values()字段
                if (value != null) {
                    sql.add(columnName);
                    values.add("?");
                    args.add(value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        return this.update(sql +values.toString(), args.toArray());
    }

    @Override
    public int insertByList(List<T> entities) {
        List args = new ArrayList();
        //自动拼接表名和主键名
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName);
        //自动拼接values的问号占位符，默认有一个问号是主键
        StringBuilder values = new StringBuilder(" values");
        //循环遍历该类的每一个属性名字
        Set<String> columnNames = columnNameMap.keySet();
        StringJoiner columnNameJoiner = new StringJoiner(",", "(", ")");
        if (!(idTypeStrategy instanceof IdTypeAssignAuto)) {
            columnNameJoiner.add(idName);
        }
        for (String columnName : columnNames) {
            columnNameJoiner.add(columnName);
        }
        sql.append(columnNameJoiner);
        for (T entity : entities) {
            StringJoiner valueJoiner = new StringJoiner(",", "(", "),");
            String idVal = idTypeStrategy.assembly(args, entity, idField);
            if (idVal != null) {
                valueJoiner.add(idVal);
            }
            for (String columnName : columnNames) {
                try {
                    Field field = entityClass.getDeclaredField(columnNameMap.get(columnName));
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    valueJoiner.add("?");
                    args.add(value);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            values.append(valueJoiner.toString());
        }
        sql.append(values.substring(0, values.length() - 1));
        return this.update(sql.toString(), args.toArray());
    }

    @Override
    public int deleteById(T entity) {
        //判断不能为空
        Objects.requireNonNull(entity);
        String sql = "delete from " + tableName + " where 1=1 ";
        List args = new ArrayList();
        sql += autoStitchingSql(entity, "and  $ = ? ", args);
        try {
            Object primaryVal = idField.get(entity);
            if (primaryVal != null) {
                sql += "and  " + idName + "= ? ";
                args.add(primaryVal);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return this.update(sql, args.toArray());
    }

    @Override
    public int updateById(T entity) {
        //判断不能为空
        Objects.requireNonNull(entity);
        String sql = "UPDATE " + tableName + " set ";
        List args = new ArrayList();
        sql += autoStitchingSql(entity, " $ = ? ,", args);
        //此时sql=UPDATE "tableName" set 属性名1 = ?,属性名2 = ?,属性名3 = ?,
        //去最后一个逗号
        sql = sql.substring(0, sql.length() - 1);
        //此时sql=UPDATE "tableName" set 属性名1 = ?,属性名2 = ?,属性名3 = ?
        sql += " where " + idName + " = ? ";
        //此时sql=UPDATE "tableName" set 属性名1 = ?,属性名2 = ?,属性名3 = ? where "primaryKey" = ?
        try {
            args.add(idField.get(entity));
            return this.update(sql, args.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<T> queryAll() {
        return this.queryByLimitAndEntity(null, null, null, null);
    }


    @Override
    public List<T> query(T entity) {
        return this.queryByLimitAndEntity(null, null, entity, null);
    }

    @Override
    public List<T> query(T equalEntity, T likeEntity) {
        return this.queryByLimitAndEntity(null, null, equalEntity, likeEntity);
    }

    public List<T> queryByLimit(Integer startPage, Integer pageSize) {
        return this.queryByLimitAndEntity(startPage, pageSize, null, null);
    }

    @Override
    public List<T> queryByLimitAndEntity(Integer startPage, Integer pageSize, T equalEntity, T likeEntity) {
        String sql = "SELECT * FROM " + this.tableName + " where 1=1 ";
        if (equalEntity == null && likeEntity == null) {
            return startPage != null && pageSize != null ?
                    this.query(sql + " LIMIT ?,?", startPage, pageSize) :
                    this.query(sql);
        }
        List args = new ArrayList();
        if (equalEntity != null) {
            sql += autoStitchingSql(equalEntity, "and $ = ? ", args);
        }
        if (likeEntity != null) {
            sql += autoStitchingSql(likeEntity, "and $ like ? ", args);
        }
        if (startPage != null && pageSize != null) {
            sql += " LIMIT ?,?";
            args.add(startPage);
            args.add(pageSize);
        }
        return this.query(sql, args.toArray());
    }

    public String autoStitchingSql(T entity, String refer, List args) {
        StringBuffer stitchingSql = new StringBuffer();
        try {
            for (String columnName : columnNameMap.keySet()) {
                Field field = entityClass.getDeclaredField(columnNameMap.get(columnName));
                field.setAccessible(true);
                Object value;
                value = field.get(entity);
                if (value != null) {
                    stitchingSql.append(refer.replace("$", columnName));
                    if (refer.indexOf("like") != -1) {
                        value = "%" + value + "%";
                    }
                    args.add(value);
                }
            }
            return stitchingSql.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getCause());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public T queryForObject(T entity) {
        List<T> resultList = query(entity);
        return resultList.isEmpty() ?
                null :
                resultList.get(0);
    }

    @Override
    public T queryById(Object id) {
        String sql = "SELECT * FROM " + this.tableName + " where " + this.idName + " = ?";
        return this.queryForObject(sql, id);
    }

    @Override
    public List<T> queryByIds(Object... ids) {
        return queryByIds(Arrays.asList(ids));
    }

    @Override
    public List<T> queryByIds(List ids) {
        Object[] prameters = new Object[ids.size()];
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(this.tableName).
                append( " where " ).append(this.idName).append(" in ") ;
        StringJoiner idJoiner = new StringJoiner(",", "(", ")");
        for (int i = 0; i < ids.size(); i++) {
            prameters[i] = ids.get(i);
            idJoiner.add("?");
        }
        sql.append(idJoiner);
        return this.query(sql.toString(), prameters);
    }

    @Override
    public T queryAsForeignKey(Object primaryEntity) {
        Object id = null;
        try {
            //获得传入对象的类型，通过class类型获得名字为此dao对应的表的主键名的属性字段的值
            Field foreignKeyField = primaryEntity.getClass().getDeclaredField(idName);
            foreignKeyField.setAccessible(true);
            id = foreignKeyField.get(primaryEntity);
            //并将获得的值作为主键查此dao对应的表数据
            return this.queryById(id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Integer count() {
        String sql = "SELECT COUNT(*) FROM  " + this.tableName;
        System.out.println("SQL: " + sql);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public T queryForObject(String sql, Object... params) {
        List<T> queryList = this.query(sql, params);
        return queryList.isEmpty() ?
                null :
                queryList.get(0);
    }

    @Override
    public List<T> query(String sql, Object... params) {
        return this.query(sql, rowMapper, params);
    }

    @Override
    public List<T> query(String sql, RowMapper<T> rowMapper, Object... params) {
        System.out.println("SQL: " + sql);
        System.out.println("ARGS: " + Arrays.toString(params));
        return params == null ?
                jdbcTemplate.query(sql, rowMapper) :
                jdbcTemplate.query(sql, rowMapper, params);
    }

    @Override
    public int update(String sql, Object... params) {
        System.out.println("SQL: " + sql);
        System.out.println("ARGS: " + Arrays.toString(params));
        return params == null ?
                jdbcTemplate.update(sql) :
                jdbcTemplate.update(sql, params);
    }
}
