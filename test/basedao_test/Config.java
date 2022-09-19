package basedao_test;

import com.kamo.bean.annotation.*;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.basedao.BaseDaoFactoryBean;
import com.kamo.proxy.annotation.EnableProxy;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableProxy
@PropertySource("db.properties")
public class Config {
    @Autowired
    private Properties db;

    @Bean
    public BaseDaoFactoryBean baseDaoFactory(DataSource dataSource) {
        return new BaseDaoFactoryBean(dataSource);
    }


    @Bean
    public DataSource dataSource(Properties db) {

        return IzumiDataSourceFactory.createDataSource(db);
    }
}