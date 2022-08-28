package rpc_test.cloud.provider.serviceimp;

import com.kamo.context_rpc.RpcService;
import rpc_test.cloud.common.service.TestService;
@RpcService(value = "tests",port = 8081 ,version = "1")
public class TestServiceImp implements TestService {
    @Override
    public Object testService(Object val) {
        return val+this.toString()+"----";
    }
}
