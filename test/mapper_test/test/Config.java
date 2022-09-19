package mapper_test.test;



import com.kamo.bean.annotation.Bean;
import com.kamo.bean.annotation.ComponentScan;
import com.kamo.bean.annotation.Configuration;
import com.kamo.bean.annotation.PropertySource;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.mapper_support.annotation.EnableMapperSupport;
import com.kamo.proxy.annotation.EnableProxy;
import com.kamo.transaction.annotation.EnableTransaction;

import javax.sql.DataSource;
import java.util.Properties;

@EnableTransaction
@EnableProxy
@EnableMapperSupport
@PropertySource("db.properties")
@Configuration
@ComponentScan
public class Config {

    @Bean
    public DataSource dataSource(Properties properties) {
        return IzumiDataSourceFactory.createDataSource(properties);
    }

}
