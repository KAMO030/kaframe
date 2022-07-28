package rpc_test.cloud.comsumer;

import com.kamo.cloud.RpcProxyFactory;
import rpc_test.cloud.common.service.TestService;

public class Comsumer {
    public static void main(String[] args) {
        TestService testService = RpcProxyFactory.getProxy(TestService.class,"2");
        Object result = testService.testService("周瑜");
        System.out.println(result);
    }
}
