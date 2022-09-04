package listener_test.com.test.main;

import com.kamo.boot.KamoApplication;
import com.kamo.boot.annotation.KamoBootApplication;
import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.Arg;
import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Bean;
import com.kamo.context.annotation.Import;
import com.kamo.context.condition.annotation.ConditionalOnBean;
import com.kamo.context.listener.ApplicationEventMulticaster;
import com.kamo.context.listener.ApplicationEventPublisher;
import com.kamo.context.listener.ApplicationListener;
import com.kamo.context.listener.impl.ContextRefreshedEvent;
import com.kamo.context.listener.impl.DefaultEventMulticaster;
import com.kamo.transaction.support.TransactionManagerBeanPostProcessor;
import listener_test.com.test.listener.TestEvent;

@KamoBootApplication(scanPath = "listener_test.com.test.listener")
@Import({TransactionManagerBeanPostProcessor.class,TransactionManagerBeanPostProcessor.class,TransactionManagerBeanPostProcessor.class})
public class ListenerTest implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ApplicationEventPublisher publisher;

    public static void main(String[] args) {
        KamoApplication.run(ListenerTest.class);
    }

    @Bean
    public ApplicationEventMulticaster  testMulticaster(){
        return new DefaultEventMulticaster();
    }
    @Bean
    @ConditionalOnBean(beanTypes = {"com.kamo.context.listener.ApplicationEventPublisher"})
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
