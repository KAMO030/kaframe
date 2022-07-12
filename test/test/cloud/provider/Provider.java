package test.cloud.provider;


import kamo.cloud.Protocol;
import kamo.cloud.ProtocolFactory;
import kamo.cloud.URL;
import test.cloud.common.service.TestService;
import test.cloud.provider.serviceimp.TestServiceImp;
import test.cloud.provider.serviceimp.TestServiceImp1;

import java.net.UnknownHostException;
public class Provider {
    public static void main(String[] args) throws UnknownHostException {

        String protocolName = "BioSocket";

        URL url = new URL(protocolName, "localhost", 8080, "1",TestService.class.getName(), TestServiceImp.class);
        URL url1 = new URL(protocolName, "localhost", 8080, "2",TestService.class.getName(), TestServiceImp1.class);

        Protocol protocol = ProtocolFactory.getProtocol(protocolName);
        protocol.export(url);
        protocol.export(url1);
    }
}
