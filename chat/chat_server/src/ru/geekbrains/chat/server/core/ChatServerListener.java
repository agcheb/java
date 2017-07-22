package ru.geekbrains.chat.server.core;

/**
 * Created by Александр on 22.06.2017.
 */
public interface ChatServerListener {

    void onChatServerLog (ChatServer chatServer,String msg);
}
