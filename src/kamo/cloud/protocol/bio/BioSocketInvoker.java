package kamo.cloud.protocol.bio;


import kamo.cloud.Invocation;
import kamo.cloud.Invoker;
import kamo.cloud.URL;


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
