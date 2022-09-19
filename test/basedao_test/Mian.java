package basedao_test;

import basedao_test.pojo.Cinfo;
import basedao_test.service.CinfoService;
import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import com.kamo.bean.annotation.Autowired;

//basedao和BFactoryBean演示demo

public class Mian {
    @Autowired
    private static CinfoService service;
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        new AnnotationConfigApplicationContext(Mian.class, Config.class);
        Cinfo cinfo = new Cinfo();
        cinfo.setcId("e");
        System.out.println(service.service(cinfo));
    }
}
