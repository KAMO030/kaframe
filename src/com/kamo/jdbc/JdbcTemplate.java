package com.kamo.jdbc;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcTemplate {
    private DataSource dataSource;

    private Map<String,List> caches = new HashMap<>();

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcTemplate() {

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(String sql, Object... obs){

        int row = 0;
        Connection connection = getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            for (int i = 0; i < obs.length; i++) {
                preparedStatement.setObject(i + 1, obs[i]);
            }
            row = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DataSourceUtils.releaseConnection(connection);
        }
        caches.clear();
        return row ;
    }

    public <T> List<T> query(String sql, Class<T> beanType, Object... obs) {
        return query(sql, new BeanPropertyRowMapper<>(beanType), obs);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... obs) {
        List<T> beanList;
        String cacheKey = sql+rowMapper+ Arrays.toString(obs);
        beanList = caches.get(cacheKey);
        if (beanList != null) {
            return beanList;
        }
        caches.put(cacheKey,null);
        Connection connection = getConnection();
        try(PreparedStatement preparedStatement = connection. prepareStatement(sql)){
            if (obs != null) {
                for (int i = 0; i < obs.length; i++) {
                    //"select * from user_info where u_id = ?"
                    preparedStatement.setObject(i + 1, obs[i]);
                }
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                beanList = new Vector<>();
                while (resultSet.next()) {
                    beanList.add(rowMapper.mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DataSourceUtils.releaseConnection(connection);
        }
        caches.put(cacheKey,beanList);
        return beanList;
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... obs) {
        List<T> list = this.query(sql, rowMapper, obs);
        if (list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }
    public <T> T queryForObject(String sql, Class<T> type, Object... obs) {
        List<T> list = this.query(sql, new SingleColumnRowMapper<>(type), obs);
        if (list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    protected Connection getConnection()  {
        return DataSourceUtils.getConnection(dataSource);
    }

}
