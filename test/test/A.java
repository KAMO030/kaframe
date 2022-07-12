package test;

import kamo.context.annotation.ComponentScan;
import kamo.context.annotation.Configuration;
import kamo.proxy.annotation.EnableProxy;

@Configuration
@ComponentScan("test")
@EnableProxy(path = "proxy")
public class A  {

//    @Bean
//    public JDBCTemplate jdbcTemplate(DataSource dataSource) {
//        return new JDBCTemplate(dataSource);
//    }
//
//
//    @Bean
//    public DataSource dataSource() {
//
//        Properties props = new Properties();
//        try {
//            props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return IzumiDataSourceFactory.createDataSource(props);
//    }
}
