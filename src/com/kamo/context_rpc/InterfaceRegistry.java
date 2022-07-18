package com.kamo.context_rpc;

import java.util.HashMap;
import java.util.Map;

public final  class InterfaceRegistry {
    private InterfaceRegistry() {
    }
    private static final Map<String,Class> interfaceMap = new HashMap<>();
    public static boolean isRegistered(String version,Class interfaceClass){
        return interfaceMap.containsKey(version+interfaceClass.getName());
    }
    public static void register(String version,Class interfaceClass){
        interfaceMap.put(version+interfaceClass.getName(), interfaceClass);
    }
}
