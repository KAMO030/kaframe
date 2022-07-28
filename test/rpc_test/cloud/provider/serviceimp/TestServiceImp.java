package rpc_test.cloud.provider.serviceimp;

import rpc_test.cloud.common.service.TestService;

public class TestServiceImp implements TestService {
    @Override
    public Object testService(Object val) {
        return val;
    }
}
