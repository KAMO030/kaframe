package ioc_test;

import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import ioc_test.dao.imp.CinfoDao1;

@Configuration
@ComponentScan
public class Config {

    @Bean
    public CinfoDao1 cinfoDao1(){
       return new CinfoDao1();
    }

//    @Bean
//    public CinfoDao2 cinfoDao2(){
//        return new CinfoDao2();
//    }
//    @Bean
//    public CinfoDao cinfoDao3(){
//        return new CinfoDao3();
//    }
}