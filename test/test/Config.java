package test;


import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.mapper_support.annotation.EnableMapperSupport;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableMapperSupport
public class Config {
    @Bean
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        ClassLoader classLoader = Config.class.getClassLoader();
        properties.load(classLoader.getResourceAsStream("db.properties"));
        return IzumiDataSourceFactory.createDataSource(properties);
    }
}
