package com.kamo.cloud;



import com.kamo.cloud.register.RemoteMapRegister;

import java.util.ArrayList;
import java.util.List;

public class ClusterInvoker implements Invoker{

    private List<Invoker> invokers = new ArrayList<>();

    public List<Invoker> getInvokers() {
        return invokers;
    }

    public void addInvokers(Invoker invoker) {
        this.invokers.add(invoker);
    }

    public static Invoker join(String interfaceName,String version){
        ClusterInvoker clusterInvoker = new ClusterInvoker();

        // 从注册中心查看urls
        List<URL> urlList = RemoteMapRegister.get(interfaceName,version);
        if (urlList==null) {
            throw new NullPointerException();
        }
        urlList.forEach(url -> {
            Protocol protocol = ProtocolFactory.getProtocol(url.getProtocol());
            Invoker invoker = protocol.refer(url);
            clusterInvoker.addInvokers(invoker);
        });

        return clusterInvoker;
    }

    @Override
    public Object invoke(Invocation invocation) {
        Invoker invoker = LoadBalance.random(invokers);
        return invoker.invoke(invocation);
    }
}
