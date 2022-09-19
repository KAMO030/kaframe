package rpc_test.cloud.comsumer;


import com.kamo.context.factory.ApplicationContext;
import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import com.kamo.bean.annotation.Configuration;
import com.kamo.bean.annotation.Import;
import com.kamo.context_rpc.Reference;
import com.kamo.context_rpc.ReferencePostProcessor;
import rpc_test.cloud.common.service.TestService;

@Configuration
@Import(ReferencePostProcessor.class)
public class Comsumer {
    @Reference
    public static TestService testService;
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Comsumer.class);
        Object result = testService.testService("周瑜");
        System.out.println(result);
        System.out.println( testService.testService("周瑜"));
    }
}
