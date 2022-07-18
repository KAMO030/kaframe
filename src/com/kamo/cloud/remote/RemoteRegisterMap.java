package com.kamo.cloud.remote;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RemoteRegisterMap {
    private RemoteRegisterMap(){}

    private static Map<String, List<URL>> remoteMap = new HashMap<>();

    public static List<URL> getURLList (String interfaceName) {
        return remoteMap.get(interfaceName);
    }

    public static void registerMap (URL url,String[] interfaceNames) {
        for (String interfaceName:interfaceNames){
            if (remoteMap.containsKey(interfaceName)){
                remoteMap.get(interfaceName).add(url);
            }else {
                List<URL> urls = new ArrayList<>();
                urls.add(url);
                remoteMap.put(interfaceName,urls);
            }
        }
        System.out.println(remoteMap);
    }
}
