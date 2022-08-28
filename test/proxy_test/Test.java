package proxy_test;

import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import com.kamo.transaction.TransactionSynchronizationManager;
import proxy_test.service.CinfoService;


//循环依赖和aop演示demo

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        CinfoService service = context.getBean(CinfoService.class);
        System.out.println(service);
        System.out.println(service.service("111"));


    }
}
