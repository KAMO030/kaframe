package com.kamo.jdbc.basedao;


import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.JdbcTemplate;
import com.kamo.jdbc.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Deprecated

public class BaseDaoImp<T> implements BaseDao<T> {
    public List<T> obj;
    protected JdbcTemplate jdbcTemplate;
    //子类dao泛型的类型对应实体类型
    protected Class entityClass;
    //表名
    protected String tableName;
    //主键属性字段对象
    protected Field primaryKeyF;
    //主键属性字段的名字
    protected String primaryKey;
    //除主键以外其他属性字段的名字与属性字段对象的map
    protected Map<String, Field> columnNameMap;
    //jdbcTemp的映射
    protected RowMapper<T> rowMapper;


    public BaseDaoImp(Class<T> entityClass) {
        this(entityClass,new JdbcTemplate());
    }

    public BaseDaoImp(Class entityClass, JdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityClass = entityClass;
        init();
    }

    public BaseDaoImp() {
        //根据反射拿到子类继承时定义的泛型类型作为dao操作的实体类的类型
        this.entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        init();
    }

    private void init() {
        this.tableName = entityClass.getSimpleName();
        this.rowMapper = new BeanPropertyRowMapper<>(entityClass);
        Field[] entityFields = entityClass.getDeclaredFields();
        this.columnNameMap = new ConcurrentHashMap<>();
        //默认实体类的第一个字段属性为主键
        this.primaryKeyF = entityFields[0];
        this.primaryKeyF.setAccessible(true);
        this.primaryKey = this.primaryKeyF.getName();
        //将除主键以外其他的属性字段压入map中
        for (int i = 1; i < entityFields.length; i++) {
            entityFields[i].setAccessible(true);
            columnNameMap.put(entityFields[i].getName(), entityFields[i]);
        }
    }

    @Override
    public int add(T entity) {
        //自动拼接表名和主键名
        String sql = "INSERT INTO " + tableName + " ( " + primaryKey + ",";
        //自动拼接values的问号占位符，默认有一个问号是主键
        String values = " values( ?,";
        List args = new ArrayList();
        //自动随机一个唯一主键
        args.add(UUID.randomUUID().toString().replace("-", ""));
        //循环遍历该类的每一个属性名字
        for (String columnName : columnNameMap.keySet()) {
            try {
                Field field = columnNameMap.get(columnName);
                Object value = field.get(entity);
                //如果传入的entity的名为columnName的属性的值不等于null，则会自动拼接Sql,和values()字段
                if (value != null) {
                    sql += columnName + ",";
                    values += "?,";
                    args.add(value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        //拼接完后需要将sql和values的最后一个逗号变为括号，并合并两个字符串
        //此时sql=INSERT INTO "tableName" (主键名,属性名1,属性名2,属性名3,<-此时结尾为逗号
        //此时values=values( ?,?,?,?,<-此时结尾为逗号
        //最后一个逗号变为括号->sql=INSERT INTO "tableName" (主键名,属性名1,属性名2,属性名3) +values( ?,?,?,? )
        sql = sql.substring(0, sql.length() - 1) + " ) " + values.substring(0, values.length() - 1) + " ) ";
        //执行上面代码后sql="INSERT INTO "tableName" (主键名,属性名1,属性名2,属性名3) values( ?,?,?,? )"
        return this.update(sql, args.toArray());
    }

    @Override
    public int delete(T entity) {
        //判断不能为空
        Objects.requireNonNull(entity);
        String sql = "delete from " + tableName + " where 1=1 ";
        List args = new ArrayList();
        sql += autoStitchingSql(entity, "and  $ = ? ", args);
        try {
            Object primaryVal = primaryKeyF.get(entity);
            if (primaryVal != null) {
                sql += "and  " + primaryKey + "= ? ";
                args.add(primaryVal);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return this.update(sql, args.toArray());
    }

    @Override
    public int update(T entity) {
        //判断不能为空
        Objects.requireNonNull(entity);
        String sql = "UPDATE " + tableName + " set ";
        List args = new ArrayList();
        sql += autoStitchingSql(entity, " $ = ? ,", args);
        //此时sql=UPDATE "tableName" set 属性名1 = ?,属性名2 = ?,属性名3 = ?,
        //去最后一个逗号
        sql = sql.substring(0, sql.length() - 1);
        //此时sql=UPDATE "tableName" set 属性名1 = ?,属性名2 = ?,属性名3 = ?
        sql += " where " + primaryKey + " = ? ";
        //此时sql=UPDATE "tableName" set 属性名1 = ?,属性名2 = ?,属性名3 = ? where "primaryKey" = ?
        try {
            args.add(primaryKeyF.get(entity));
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
                Field field = columnNameMap.get(columnName);
                Object value = null;
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
            throw new RuntimeException(e);
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
        String sql = "SELECT * FROM " + this.tableName + " where " + this.primaryKey + " = ?";
        return this.queryForObject(sql, id);
    }

    @Override
    public T queryAsForeignKey(Object primaryEntity) {
        Object id = null;
        try {
            //获得传入对象的类型，通过class类型获得名字为此dao对应的表的主键名的属性字段的值
            Field foreignKeyField = primaryEntity.getClass().getDeclaredField(primaryKey);
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
