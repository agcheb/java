package ru.geekbrains.network.samples;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Александр on 03.07.2017.
 */
public class SimpleServer {
    public static void main(String[] args){
        try (ServerSocket serverSocket = new ServerSocket(8189);
             Socket socket = serverSocket.accept();) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out =new DataOutputStream(socket.getOutputStream());
            while (true){
                out.writeUTF("echo: " + in.readUTF());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
