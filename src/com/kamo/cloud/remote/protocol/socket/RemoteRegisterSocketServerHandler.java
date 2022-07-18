package com.kamo.cloud.remote.protocol.socket;

import com.kamo.cloud.remote.RemoteRegisterMap;
import com.kamo.cloud.remote.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RemoteRegisterSocketServerHandler {
    public void handler(Socket socket) throws IOException {
        try {
            String line = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if (line.matches("^Register:(.){0,}")) {
                line = line.replaceAll("Register:","");
                String[] split = line.split("&");
                String serverIP = split[0];
                Integer serverPort = Integer.valueOf(split[1]);
                URL url = new URL(serverIP, serverPort);
                String[] interfaceNames = new String[split.length-2];
                for (int i=2;i<split.length;i++){
                    interfaceNames[i-2] = split[i];
                }
                RemoteRegisterMap.registerMap(url,interfaceNames);
            }else {
                URL url = RemoteRegisterMap.getURLList(line).get(0);
                new PrintWriter(socket.getOutputStream(),true).println(url.getIpAddress()+':'+url.getPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket!=null){
                socket.close();
            }
        }
    }
}
