package basedao_test;

import basedao_test.service.CinfoService;
import basedao_test.service.pojo.Cinfo;
import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;

//basedao和BFactoryBean演示demo
public class Mian {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        CinfoService cinfoServiceImp = (CinfoService) context.getBean("cinfoServiceImp");
        Cinfo cinfo = new Cinfo();
        cinfo.setcName("可");
        System.out.println(cinfoServiceImp.service(cinfo));
    }
}
