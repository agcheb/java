package ru.geekbrains.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Александр on 03.07.2017.
 */
public class SocketThread extends Thread {

    private final SocketThreadListener eventListener;
    private final Socket socket;
    DataOutputStream out;


public SocketThread(SocketThreadListener eventListener, String name, Socket socket){
       super(name);
       this.eventListener = eventListener;
       this.socket = socket;
       start();
}

    @Override
    public void run() {
    eventListener.onStartSocketThread(this);
    try {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        eventListener.onReadySocketThread(this,socket);
        while (!isInterrupted()){
            String msg = in.readUTF();
            eventListener.onRecieveString(this,socket,msg);
        }
    }
    catch (IOException e){

    }
    finally {
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onExceptionSocketThread(this,socket,e);
        }
        eventListener.onStopSocketThread(this);
    }

    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            eventListener.onExceptionSocketThread(this,socket,e);
        }
    }

    public synchronized void close(){
        interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onExceptionSocketThread(this,socket,e);
        }
    }
}
