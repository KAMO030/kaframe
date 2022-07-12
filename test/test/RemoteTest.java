package test;

import cn.com.hamster.bean.UserInfo;
import kamo.datasource.IzumiDataSourceFactory;
import kamo.jdbc.JDBCTemplate;
import kamo.jdbc.RowMapper;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RemoteTest {
    public static void main(String[] args) throws IOException {
//        RemoteRegisterSocketServer socketServer = new RemoteRegisterSocketServer();
//        socketServer.start(1112);
        Properties properties = new Properties();
        properties.load(new FileReader("jdbc.properties"));
        JDBCTemplate jdbcTemplate = new JDBCTemplate(IzumiDataSourceFactory.createDataSource(properties));
        jdbcTemplate.query("", new RowMapper<UserInfo>() {
            @Override
            public UserInfo mapRow(ResultSet resultSet) throws SQLException {
                UserInfo userInfo = new UserInfo();
                userInfo.setuId(resultSet.getString("uId"));

                return null;
            }
        });
    }
}
