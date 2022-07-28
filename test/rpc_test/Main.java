package rpc_test;

import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import com.kamo.context.annotation.Configuration;
import com.kamo.context.annotation.Import;
import com.kamo.context_rpc.Reference;
import com.kamo.context_rpc.ReferencePostProcessor;
import rpc_test.cloud.common.service.TestService;
@Configuration
@Import(ReferencePostProcessor.class)
public class Main {
    @Reference("2")
    private static TestService testService;
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Main.class);
        System.out.println(testService.testService("哈哈"));
    }
}
