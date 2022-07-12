package kamo.cloud.protocol.bio;

import kamo.cloud.Invocation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;

public class BioSocketClient {
    public  Object send(String hostname , Integer port, Invocation invocation){
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(hostname,port));
            new ObjectOutputStream(socket.getOutputStream()).writeObject(invocation);
            return new ObjectInputStream(socket.getInputStream()).readObject();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (socket!=null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
