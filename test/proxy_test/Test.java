package proxy_test;

import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.impl.ContextRefreshedListenerMethodAdapter;
import proxy_test.service.CinfoService;
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
