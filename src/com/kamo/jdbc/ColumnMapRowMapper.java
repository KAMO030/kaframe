package com.kamo.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColumnMapRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            resultMap.put(metaData.getColumnName(i),resultSet.getObject(i));
        }
        return resultMap;
    }
}
