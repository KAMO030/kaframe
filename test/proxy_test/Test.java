package proxy_test;

import com.kamo.context.listener.impl.ContextRefreshedListenerMethodAdapter;


//循环依赖和aop演示demo

public class Test {

    public static void main(String[] args) {
        System.out.println(new ContextRefreshedListenerMethodAdapter(null, null).supportsEventType());
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
