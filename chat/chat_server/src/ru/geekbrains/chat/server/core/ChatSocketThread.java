package ru.geekbrains.chat.server.core;

import ru.geekbrains.chat.libraries.Messages;
import ru.geekbrains.network.SocketThread;
import ru.geekbrains.network.SocketThreadListener;

import java.net.Socket;

/**
 * Created by Александр on 03.07.2017.
 */
public class ChatSocketThread extends SocketThread {

    private boolean isAuthorized;
    private String nick;

    public ChatSocketThread(SocketThreadListener eventListener, String name, Socket socket) {
        super(eventListener, name, socket);
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    void authorizeAccept(String nick){
        this.isAuthorized = true;
        this.nick = nick;
        sendMsg(Messages.getAuthAccept(nick));
    }

    void setAuthorized(boolean isAuthorized){
        this.isAuthorized = isAuthorized;
    }

    void authError(){
        sendMsg(Messages.AUTH_ERROR);
        close();
    }

    void messageFormatError(String msg){
        sendMsg(Messages.getMsgFormatError(msg));
        close();
    }
    String getNick(){
        return nick;
    }
}
