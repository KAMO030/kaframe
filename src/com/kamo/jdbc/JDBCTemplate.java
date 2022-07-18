package com.kamo.jdbc;


import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class JDBCTemplate {
    private DataSource dataSource;

    public JDBCTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JDBCTemplate() {

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(String sql, Object... obs){
        PreparedStatement preparedStatement = null;
        int row = 0;

        try {
            preparedStatement = dataSource.getConnection().prepareStatement(sql);
            for (int i = 0; i < obs.length; i++) {
                preparedStatement.setObject(i + 1, obs[i]);
            }
            row = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row ;
    }



    public <T> List<T> query(String sql, Class<T> beanType, Object... obs) {
        return query(sql, new BeanPropertyRowMapper<>(beanType), obs);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... obs) {
        List<T> beanList = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = dataSource.getConnection().prepareStatement(sql);
            if (obs != null) {
                for (int i = 0; i < obs.length; i++) {
                    //"select * from user_info where u_id = ?"
                    preparedStatement.setObject(i + 1, obs[i]);
                }
            }
            System.out.println("SQL: "+sql);
            System.out.println("OBJ: "+ Arrays.toString(obs));
            resultSet = preparedStatement.executeQuery();
            beanList = new Vector<>();
            while (resultSet.next()) {
                beanList.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
        List<T> list = this.query(sql, new SimpleRowMapper<>(type), obs);
        if (list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

}
