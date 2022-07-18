package proxy_test;

import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import com.kamo.datasource.IzumiDataSourceFactory;
import com.kamo.jdbc.basedao.BaseDaoFactory;
import com.kamo.proxy.annotation.EnableProxy;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableProxy(path = "proxy_test")
public class Config {


}