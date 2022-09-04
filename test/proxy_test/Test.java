package proxy_test;

import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import com.kamo.context.listener.ApplicationEvent;
import com.kamo.context.listener.ApplicationListener;
import com.kamo.context.listener.impl.ContextRefreshedListenerAdapter;
import com.kamo.transaction.TransactionSynchronizationManager;
import proxy_test.service.CinfoService;
import proxy_test.service.imp.CinfoServiceImp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


//循环依赖和aop演示demo

public class Test {

    public static void main(String[] args) {
        System.out.println(new ContextRefreshedListenerAdapter(null, null).getEventType());
//        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//        CinfoService service = context.getBean(CinfoService.class);
//        context.publishEvent(new ApplicationEvent("11"));
//        service.service("11");
//        System.out.println(service);
//        System.out.println(service.service("111"));
//        CinfoServiceImp cinfoService = new CinfoServiceImp();
//        System.out.println(cinfoService.getEventType());

    }
}
