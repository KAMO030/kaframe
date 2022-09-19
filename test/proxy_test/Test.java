package proxy_test;

import com.kamo.context.factory.ApplicationContext;
import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import proxy_test.service.imp.CinfoServiceImp;


//循环依赖和aop演示demo

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        CinfoServiceImp service = context.getBean(CinfoServiceImp.class);

        service.service("11");
//        System.out.println(service);
//        System.out.println(service.service("111"));



    }
}
