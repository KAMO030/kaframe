package listener_test.com.test.listener;

import com.kamo.context.annotation.*;
import com.kamo.context.listener.ApplicationEventPublisher;
import com.kamo.context.listener.annotation.Listener;
import com.kamo.context.listener.impl.ContextRefreshedEvent;
import com.kamo.context.listener.impl.ContextRefreshedListenerMethodAdapter;

@Component
public class ListenerBean {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Listener(eventType = ContextRefreshedEvent.class,multicasterNames = {"testMulticaster","defaultEventMulticaster"})
    public void contextRefreshedListener(ContextRefreshedEvent event){
        System.out.println("contextRefreshedListener----"+event.getApplicationContext());
    }
    @Listener
    public void contextRefreshedListener2(ContextRefreshedEvent event){
        publisher.publishEvent(event);
        System.out.println("contextRefreshedListener----2"+event.getApplicationContext());
    }
    @Listener(listenerType = ContextRefreshedListenerMethodAdapter.class)
    public void contextRefreshedListener3(@Arg(name = "source") AnnotationConfigApplicationContext applicationContext, ContextRefreshedEvent event){
        System.out.println("contextRefreshedListener----31231"+applicationContext);
        System.out.println("contextRefreshedListener----31231"+event);
    }
    @Listener(eventType = TestEvent.class)
    public void testEventListener(String test,@Arg(name = "test1") String test1){
        System.out.println("testEventListener----"+test);
        System.out.println("testEventListener----"+test1);
    }
}
