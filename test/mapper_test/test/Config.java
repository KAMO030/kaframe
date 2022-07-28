package mapper_test.test;


import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import com.kamo.context.annotation.PropertySource;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.mapper_upport.annotation.EnableMapperSupport;
import com.kamo.transaction.annotation.EnableTransaction;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableTransaction
@EnableMapperSupport
@PropertySource("db.properties")
public class Config {

    @Bean
    public DataSource dataSource(Properties properties) {
        return IzumiDataSourceFactory.createDataSource(properties);
    }

}
