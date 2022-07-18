package com.kamo.cloud.protocol.bio;


import com.kamo.cloud.Invocation;
import com.kamo.cloud.Invoker;
import com.kamo.cloud.URL;


public class BioSocketInvoker implements Invoker {

    private URL url;

    public BioSocketInvoker(URL url) {
        this.url = url;
    }

    @Override
    public Object invoke(Invocation invocation) {
        BioSocketClient client = new BioSocketClient();
        return client.send(url.getHostname(),url.getPort(), invocation);
    }

}
