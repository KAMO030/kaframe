package com.kamo.idal;

import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.JdbcTemplate;
import com.kamo.jdbc.RowMapper;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSession {
    private DataSource dataSource;
    private Map<String, Mapper> mapperMap;
    private JdbcTemplate jdbcTemplate;
    private Map<String, Object> proxyMap;
    private Map<String, RowMapper> rowMapperMap;

    SqlSession(DataSource dataSource, Map<String, Mapper> mapperMap) {
        proxyMap = new HashMap<>();
        this.dataSource = dataSource;
        this.mapperMap = mapperMap;
        jdbcTemplate = new JdbcTemplate(dataSource);
        rowMapperMap = new HashMap<>();
    }


    public <T> T getMapper(Class<T> type) {
        if (type == null) {
            throw new RuntimeException();
        }
        Mapper mapper = mapperMap.get(type.getName());

        if (proxyMap.containsKey(type.getName())) {
            return (T) proxyMap.get(type.getName());
        }
        MapperProxy mapperProxy = new MapperProxy(mapper, this);
        T proxy = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, mapperProxy);
        proxyMap.put(type.getName(), proxy);
        return proxy;
    }

    public List selectList(String mapperClass, Object... params) {
        int index = mapperClass.lastIndexOf('.');
        String mapperPath = mapperClass.substring(0, index);
        String mapperID = mapperClass.substring(index + 1);
        Mapper mapper = mapperMap.get(mapperPath);
        Map<String, Object> selectMapByID = mapper.getSelectByID(mapperID);
        if (selectMapByID == null) {
            throw new NullPointerException();
        }

        String sql = (String) selectMapByID.get("sql");
        if (sql == null || sql.isEmpty()) {
            throw new NullPointerException();
        }
        Class resultType = (Class) selectMapByID.get("resultType");
        RowMapper rowMapper;
        if (resultType != null) {
            if (rowMapperMap.containsKey(resultType.getName())) {
                rowMapper = rowMapperMap.get(resultType.getName());
            } else {
                rowMapper = new BeanPropertyRowMapper(resultType);
                rowMapperMap.put(resultType.getName(), rowMapper);
            }
        } else {
            rowMapper = mapper.getResultMapByID((String) selectMapByID.get("resultMap"));
            if (rowMapper == null) {
                throw new NullPointerException();
            }
        }
        String paramType = (String) selectMapByID.get("paramType");
        List<String> paraNames = (List) selectMapByID.get("paraNames");


        String select = (String) selectMapByID.get("select");
        try {
            if (paramType != null && !paramType.equals("")) {
                if (paraNames != null && !paraNames.isEmpty()) {
                    if (!"auto".equalsIgnoreCase(paramType)) {
                        params = getparam(paraNames, Class.forName(paramType), params);
                    } else {
                        params = getparam(paraNames, params[0].getClass(), params);
                    }
                }
            } else if (!select.isEmpty()) {
                if (select.indexOf('.') == -1) {
                    select = mapperPath + '.' + select;
                }
                Object otherResult = selectOne(select);
                Class otherType = otherResult.getClass();
                Field paraField;
                params = new Object[paraNames.size()];
                for (int i=0;i<paraNames.size();i++){
                    paraField = otherType.getDeclaredField(paraNames.get(i));
                    paraField.setAccessible(true);
                    params[i]=paraField.get(otherResult);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    public Object selectOne(String mapperClass, Object... params) {
        List query = selectList(mapperClass, params);
        if (query.isEmpty()){
            return null;
        }
        return query.get(0);
    }

    public boolean isUpdate(String mapperClass, Object... params) {
        return updateRow(mapperClass, params) > 0;
    }

    public int updateRow(String mapperClass, Object... params) {
        int i = mapperClass.lastIndexOf('.');
        String mapperPath = mapperClass.substring(0, i);
        String mapperID = mapperClass.substring(i + 1);
        Map<String, Object> updateMapByID = mapperMap.get(mapperPath).getUpDateByID(mapperID);
        if (updateMapByID == null) {
            throw new NullPointerException();
        }
        String sql = (String) updateMapByID.get("sql");
        if (sql == null || sql.isEmpty()) {
            throw new NullPointerException();
        }
        String paramType = (String) updateMapByID.get("paramType");
        List paraNames = (List) updateMapByID.get("paraNames");

        try {
            if (paramType != null && !paramType.equals("")) {
                if (paraNames != null && !paraNames.isEmpty()) {
                    if (!"auto".equalsIgnoreCase(paramType)) {
                        params = getparam(paraNames, Class.forName(paramType), params);
                    } else {
                        params = getparam(paraNames, params[0].getClass(), params);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return jdbcTemplate.update(sql, params);
    }

    private Object[] getparam(List<String> paraNames, Class type, Object[] params) throws IllegalAccessException, NoSuchFieldException {
        int size = paraNames.size();
        Object[] args = new Object[size + params.length - 1];
        Field paramField;
        for (int i = 0; i < paraNames.size(); i++) {
            paramField = type.getDeclaredField(paraNames.get(i));
            paramField.setAccessible(true);
            args[i] = paramField.get(params[0]);
        }
        for (int i = 1; i < params.length; i++) {
            args[i] = params[i];
        }
        return args;
    }

    public void rollBack() {
        try {
            dataSource.getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            dataSource.getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setAutoCommit(boolean isAuto) {
        try {
            dataSource.getConnection().setAutoCommit(isAuto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
