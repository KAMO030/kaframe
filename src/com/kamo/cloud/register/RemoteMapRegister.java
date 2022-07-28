package com.kamo.cloud.register;


import com.kamo.cloud.URL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteMapRegister {

    private static Map<String, List<URL>> REGISTER = new HashMap<>();


    public static void regist(URL url){
        String interfaceKey = getInterfaceKey(url.getInterfaceName(), url.getVersion());
        List<URL> list = REGISTER.get(interfaceKey);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(url);

        REGISTER.put(interfaceKey, list);

        saveFile();
    }

    public static List<URL> get(String interfaceName,String version) {
        REGISTER = getFile();
        List<URL> list = REGISTER.get(getInterfaceKey(interfaceName,version));
        return list;
    }

    private static  String getInterfaceKey(String interfaceName,String version){
        return interfaceName + version;
    }


    private static void saveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("temp1.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(REGISTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<URL>> getFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("temp1.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Map<String, List<URL>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
