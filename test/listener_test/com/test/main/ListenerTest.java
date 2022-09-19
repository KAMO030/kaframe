package listener_test.com.test.main;

import com.kamo.bean.annotation.*;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import com.kamo.context.condition.annotation.ConditionalOnBean;
import com.kamo.context.listener.ApplicationEventMulticaster;
import com.kamo.context.listener.ApplicationEventPublisher;
import com.kamo.context.listener.ApplicationListener;
import com.kamo.context.listener.impl.ContextRefreshedEvent;
import com.kamo.context.listener.impl.DefaultEventMulticaster;
import listener_test.com.test.listener.TestEvent;

@ComponentScan( "listener_test.com.test.listener")
@Configuration
public class ListenerTest implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ApplicationEventPublisher publisher;

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ListenerTest.class);
    }

    @Bean
    public ApplicationEventMulticaster  testMulticaster(){
        return new DefaultEventMulticaster();
    }
    @Bean
    @ConditionalOnBean(beanClasses = ApplicationEventPublisher.class)
    public ApplicationEventPublisher publisher(@Arg(name = "testMulticaster")ApplicationEventMulticaster multicaster ){
        return (applicationEvent)->{multicaster.multicastEvent(applicationEvent);};
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        System.out.println("onApplicationEvent----"+ applicationContext);
        applicationContext.publishEvent(new TestEvent(this));
        publisher.publishEvent(new ContextRefreshedEvent(applicationContext));
    }


}
