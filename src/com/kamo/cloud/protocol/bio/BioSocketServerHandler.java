package com.kamo.cloud.protocol.bio;

import com.kamo.cloud.Invocation;
import com.kamo.cloud.register.LocalRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class BioSocketServerHandler {
    private Map<Class,Object> impMap = new HashMap<>();
    public void handler(Socket socket) throws IOException {
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(socket.getInputStream()).readObject();

            Object implObject =  LocalRegister.get(invocation.getInterfaceName(),invocation.getVersion());
            Class implClass = null;
            if(implObject instanceof String){
                implClass = Thread.currentThread().getContextClassLoader().loadClass((String) implObject);
                implObject = impMap.get(implClass);
                if (implObject == null) {
                    implObject = implClass.newInstance();
                    impMap.put(implClass, implObject);
                }
            }else {
                implClass = implObject.getClass();
                if (!impMap.containsKey(implClass)) {
                    impMap.put(implClass, implObject);
                }
            }

            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
            Object result = method.invoke(implObject, invocation.getParams());

            new ObjectOutputStream(socket.getOutputStream()).writeObject(result);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            if (socket!=null) {
                socket.close();
            }
        }
    }
}
