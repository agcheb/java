package ru.geekbrains.chat.server.core;

/**
 * Created by Александр on 03.07.2017.
 */
public interface SecurityManager {

    void init();
    String getNick(String login, String password);
    void dispose();

}
