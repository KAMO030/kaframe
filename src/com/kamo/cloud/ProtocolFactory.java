package com.kamo.cloud;


import com.kamo.cloud.protocol.bio.BioSocketProtocol;

import java.util.HashMap;
import java.util.Map;


public class ProtocolFactory {
   private static Map<String,Protocol> PROTOCOL_CACHE = new HashMap<>();
    public static Protocol getProtocol(String name) {
        if (PROTOCOL_CACHE.containsKey(name)) {
            return PROTOCOL_CACHE.get(name);
        }
//        switch (name) {
//            case "BioSocket":
//                return  new BioSocketProtocol();
//            case "dubbo":
////                return new DubboProtocol();
//            default:
//                break;
//        }
        Protocol protocol = new BioSocketProtocol();
        PROTOCOL_CACHE.put(name, protocol);
        return protocol;
    }
}
