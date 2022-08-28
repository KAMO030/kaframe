package ioc_test;

import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import ioc_test.service.CinfoService;

//basedao和BFactoryBean演示demo
public class Mian {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        CinfoService cinfoServiceImp = context.getBean("cinfoServiceImp");
        cinfoServiceImp.service();
        System.out.println(cinfoServiceImp);
    }
}
