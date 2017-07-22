package ru.geekbrains.network;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Александр on 03.07.2017.
 */
public interface SocketThreadListener {

    void onStartSocketThread(SocketThread thread);
    void onStopSocketThread(SocketThread thread);

    void onReadySocketThread(SocketThread thread, Socket socket);
    void onRecieveString(SocketThread thread, Socket socket,String value);

    void onExceptionSocketThread(SocketThread thread,Socket socket, Exception e);
}
