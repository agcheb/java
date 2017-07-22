package ru.geekbrains.chat.libraries;

import javax.jws.soap.SOAPBinding;

/**
 * Created by Александр on 03.07.2017.
 */
public class Messages {

    public static final String DELIMITER = ";";
    public static final String AUTH_REQUEST = "/auth_request";
    public static final String AUTH_ACCEPT = "/auth_accept";
    public static final String AUTH_ERROR = "/auth_error";
    public static final String USER_LIST = "/user_list";
    public static final String RECONNECT = "/reconnect";
    public static final String BROADCAST = "/bcast";
    public static final String MSG_FORMAT_ERROR = "/msg_format_error";

    // auth request login, password
    public static String getAuthRequest(String login, String password){
        return AUTH_REQUEST + DELIMITER + login +DELIMITER +password;
    }

    // auth authAccept nick
    public static String getAuthAccept(String nick){
        return AUTH_ACCEPT + DELIMITER + nick;
    }

    // broadcast time src value
    public  static  String getBroadcast(String src, String value){
        return  BROADCAST + DELIMITER + System.currentTimeMillis() + DELIMITER + src + DELIMITER + value;
    }

    // user_list time user1 user2 ....
    public static String getUserList(String users){
        return USER_LIST + DELIMITER + users ;
    }

    // auth_error
    public static String getAuthError(){
        return AUTH_ERROR;
    }

    // msg_format_error  time value
    public static String getMsgFormatError(String value){
        return MSG_FORMAT_ERROR +DELIMITER + value;
    }
}
