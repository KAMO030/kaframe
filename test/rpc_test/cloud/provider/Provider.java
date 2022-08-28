package rpc_test.cloud.provider;


import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import com.kamo.context.annotation.Import;
import com.kamo.context_rpc.ReferencePostProcessor;
import rpc_test.cloud.common.service.TestService;
import rpc_test.cloud.provider.serviceimp.TestServiceImp;

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
