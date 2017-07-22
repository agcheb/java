package ru.geekbrains.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Александр on 03.07.2017.
 */
public class ServerSocketThread extends Thread{

private final int port;
private final int timeout;
private final ServerSocketThreadListener eventListener;

    public ServerSocketThread(ServerSocketThreadListener eventListener,String name, int port, int timeout){
        super(name);
        this.eventListener = eventListener;
    this.port = port;
    this.timeout = timeout;
    start();
    }

    @Override
    public void run(){

        eventListener.onStartServerSocketThread(this);
        //System.out.println("ServerSocketThread запущен");
        while (!isInterrupted()){
        //    System.out.println("ServerSocketThread работает");
            try(ServerSocket serverSocket = new ServerSocket(port);) {
                serverSocket.setSoTimeout(timeout);
                eventListener.onReadyServerSocketThread(this,serverSocket);
                while (!isInterrupted()) {
                    Socket socket;
                    try {
                        socket = serverSocket.accept();
                    } catch (SocketTimeoutException e) {
                        eventListener.onTimeOutAccept(this, serverSocket);
                        //System.out.println("Случился timeout");
                        continue;
                    }
                    eventListener.onAcceptedSocket(this,serverSocket,socket);
                    // Работа с сокетом

                }
            }
            catch (IOException e){
          //     throw new RuntimeException(e);
              eventListener.onExceptionServerSocketThread(this, e);
            }
            finally {
                eventListener.onStopServerSocketThread(this);
                //System.out.println("ServerSocketThread остановлен");
            }
        }
    }
}
