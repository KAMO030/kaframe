package com.kamo.cloud.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    private static Map<String, Object> map = new HashMap<>();


    public static void register(String interfaceName, String version, Object instance) {
        map.put(interfaceName+version, instance);
    }
    public static Object get(String interfaceName,String version) {
       return map.get(interfaceName+version);
    }
}
