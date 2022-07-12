package kamo.cloud.remote.protocol.socket;

import java.io.IOException;
import java.net.ServerSocket;

public class RemoteRegisterSocketServer {
    private ServerSocket socket;
    private RemoteRegisterSocketServerHandler handler = new RemoteRegisterSocketServerHandler();
    public void start(int port){
        try {
            socket = new ServerSocket(port);
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
