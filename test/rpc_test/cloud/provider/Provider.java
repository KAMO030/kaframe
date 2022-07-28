package rpc_test.cloud.provider;


import com.kamo.cloud.Protocol;
import com.kamo.cloud.ProtocolFactory;
import com.kamo.cloud.URL;
import rpc_test.cloud.common.service.TestService;
import rpc_test.cloud.provider.serviceimp.TestServiceImp;
import rpc_test.cloud.provider.serviceimp.TestServiceImp1;

import java.net.UnknownHostException;
public class Provider {
    public static void main(String[] args) throws UnknownHostException {

        String protocolName = "BioSocket";

        URL url = new URL(protocolName, "localhost", 8080, "1", TestService.class.getName(), TestServiceImp.class);
        URL url1 = new URL(protocolName, "localhost", 8080, "2",TestService.class.getName(), TestServiceImp1.class);

        Protocol protocol = ProtocolFactory.getProtocol(protocolName);
        protocol.export(url);
        protocol.export(url1);
    }
}
