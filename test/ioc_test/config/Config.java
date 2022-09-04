package ioc_test.config;

import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.condition.annotation.Conditional;
import com.kamo.context.annotation.Configuration;
import ioc_test.dao.imp.CinfoDao1;
import ioc_test.service.imp.CinfoServiceImp;

//@Configuration
//@ComponentScan("ioc_test")
public class Config {

    @Bean
    @Conditional(CinfoServiceImp.ConditionTest.class)
    public CinfoDao1 cinfoDao121(){
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