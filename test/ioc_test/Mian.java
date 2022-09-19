package ioc_test;


import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import com.kamo.bean.annotation.ComponentScan;
import com.kamo.bean.annotation.Configuration;
import ioc_test.service.CinfoService;

@Configuration
@ComponentScan
public class Mian {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InterruptedException {
//        WebClassLoader webClassLoader = new WebClassLoader();
//        Thread.currentThread().setContextClassLoader(webClassLoader);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Mian.class);
        CinfoService cinfoServiceImp = context.getBean("cinfoServiceImp");
        cinfoServiceImp.service();
        context.destroy();
        CinfoService cinfoServiceImp1 = context.getBean("cinfoServiceImp");
        cinfoServiceImp1.service();
        System.out.println(cinfoServiceImp.equals(cinfoServiceImp1));
//        List list = new ArrayList();

    }
}

