package ioc_test;

import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.basedao.BaseDaoFactory;
import ioc_test.dao.CinfoDao;
import ioc_test.dao.imp.CinfoDao1;
import ioc_test.dao.imp.CinfoDao2;
import ioc_test.dao.imp.CinfoDao3;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan
public class Config {

    @Bean
    public CinfoDao1 cinfoDao1(){
       return new CinfoDao1();
    }

    @Bean
    public CinfoDao2 cinfoDao2(){
        return new CinfoDao2();
    }
//    @Bean
//    public CinfoDao cinfoDao3(){
//        return new CinfoDao3();
//    }
}