package basedao_test;

import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.basedao.BaseDaoFactory;


import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan
public class Config  {

    @Bean
    public BaseDaoFactory baseDaoFactory(DataSource dataSource) {
        return new BaseDaoFactory(dataSource);
    }


    @Bean
    public DataSource dataSource() {

        Properties props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return IzumiDataSourceFactory.createDataSource(props);
    }
}