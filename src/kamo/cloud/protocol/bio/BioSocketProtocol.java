package kamo.cloud.protocol.bio;

import kamo.cloud.Invoker;
import kamo.cloud.Protocol;
import kamo.cloud.URL;

import kamo.cloud.register.LocalRegister;
import kamo.cloud.register.RemoteMapRegister;

public class BioSocketProtocol implements Protocol {
    private BioSocketServer server;
    @Override
    public void export(URL url) {
        LocalRegister.regist(url.getInterfaceName(), url.getVersion(),url.getImplClass());
        RemoteMapRegister.regist(url);
        if (server==null){
            server = new BioSocketServer();
            new Thread(()-> server.start(url.getHostname(), url.getPort())).start();
        }
    }

    @Override
    public Invoker refer(URL url) {
        return new BioSocketInvoker(url);
    }

}
