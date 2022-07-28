package com.kamo.cloud.protocol.bio;

import com.kamo.cloud.Invoker;
import com.kamo.cloud.Protocol;
import com.kamo.cloud.URL;
import com.kamo.cloud.register.LocalRegister;
import com.kamo.cloud.register.RemoteMapRegister;

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
