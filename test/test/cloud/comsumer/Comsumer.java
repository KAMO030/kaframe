package test.cloud.comsumer;

import kamo.cloud.RpcProxyFactory;

import test.cloud.common.service.TestService;

import java.sql.SQLException;

public class Comsumer {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        TestService testService = RpcProxyFactory.getProxy(TestService.class,"1");
        Object result = testService.testService("周瑜");
        System.out.println(result);
    }
}
