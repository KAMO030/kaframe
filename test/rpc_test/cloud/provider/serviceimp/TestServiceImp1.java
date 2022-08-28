package rpc_test.cloud.provider.serviceimp;

import com.kamo.context_rpc.RpcService;
import rpc_test.cloud.common.service.TestService;
@RpcService(port = 8081)
public class TestServiceImp1 implements TestService {
    @Override
    public Object testService(Object val) {
        return val+"12"+this.toString()+"----";
    }
}
