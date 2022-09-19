package rpc_test.cloud.provider;


import com.kamo.context.factory.ApplicationContext;
import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import com.kamo.bean.annotation.ComponentScan;
import com.kamo.bean.annotation.Configuration;

@Configuration
@ComponentScan("rpc_test.cloud.provider.serviceimp")
//@Import(ReferencePostProcessor.class)
public class Provider {
    public static void main(String[] args)  {
        ApplicationContext context = new AnnotationConfigApplicationContext(Provider.class);
        Object testServiceImp = context.getBean("tests");

        System.out.println(testServiceImp);
    }
}
