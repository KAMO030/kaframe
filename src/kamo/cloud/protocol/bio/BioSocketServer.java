package kamo.cloud.protocol.bio;


import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class BioSocketServer {
    private ServerSocket socket;
    private BioSocketServerHandler handler = new BioSocketServerHandler();
    public void start(String hostName , Integer port){
        try {
            socket = new ServerSocket();
            socket.bind(new InetSocketAddress(hostName,port));
            while (true){
                handler.handler(socket.accept());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
