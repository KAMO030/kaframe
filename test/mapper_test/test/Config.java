package mapper_test.test;


import com.kamo.boot.annotation.KamoBootApplication;
import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.PropertySource;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.mapper_upport.annotation.EnableMapperSupport;
import com.kamo.proxy.annotation.EnableProxy;
import com.kamo.transaction.annotation.EnableTransaction;

import javax.sql.DataSource;
import java.util.Properties;

@EnableTransaction
@EnableProxy
@EnableMapperSupport
@PropertySource("db.properties")
@KamoBootApplication
//@Configuration
//@ComponentScan
public class Config {

    @Bean
    public DataSource dataSource(Properties properties) {
        return IzumiDataSourceFactory.createDataSource(properties);
    }

}
