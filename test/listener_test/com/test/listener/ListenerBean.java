package listener_test.com.test.listener;

import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Component;
import com.kamo.context.listener.ApplicationEventPublisher;
import com.kamo.context.listener.annotation.Listener;
import com.kamo.context.listener.impl.ContextRefreshedEvent;
import com.kamo.context.listener.impl.ContextRefreshedListenerAdapter;

@Component
public class ListenerBean {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Listener(eventType = ContextRefreshedEvent.class,multicasterNames = {"testMulticaster","com.kamo.context.listener.impl.DefaultEventMulticaster"})
    public void contextRefreshedListener(ContextRefreshedEvent event){
        System.out.println("contextRefreshedListener----"+event.getApplicationContext());
    }
    @Listener(eventType = ContextRefreshedEvent.class)
    public void contextRefreshedListener2(ContextRefreshedEvent event){
        publisher.publishEvent(event);
        System.out.println("contextRefreshedListener----2"+event.getApplicationContext());
    }
    @Listener(listenerType = ContextRefreshedListenerAdapter.class)
    public void contextRefreshedListener3(ContextRefreshedEvent event){
        System.out.println("contextRefreshedListener----3"+event.getApplicationContext());
    }
    @Listener(eventType = TestEvent.class)
    public void testEventListener(TestEvent event){
        System.out.println("testEventListener----"+event.getSource());
    }
}
