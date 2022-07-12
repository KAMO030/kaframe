package kamo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleRowMapper<T> implements RowMapper<T>{


    public SimpleRowMapper(Class<T> type) {

    }


    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        if(resultSet.getMetaData().getColumnCount()!=1){
            throw new SQLException("");
        }
        return (T) resultSet.getObject(1);
    }
}
