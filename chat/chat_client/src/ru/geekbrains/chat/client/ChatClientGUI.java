package ru.geekbrains.chat.client;

import ru.geekbrains.chat.libraries.Messages;
import ru.geekbrains.network.SocketThread;
import ru.geekbrains.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Александр on 22.06.2017.
 */
public class ChatClientGUI extends JFrame implements ActionListener,Thread.UncaughtExceptionHandler,SocketThreadListener {

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatClientGUI();
            }
        });
    }
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss: ");
   private static final int WIDTH = 800;
    private static final int HEIGHT = 300;
    private static final String TITLE = "Chat client";
    private final DefaultListModel<String> chatclients = new DefaultListModel<>();
//    private final ArrayList<String> chatclients = new ArrayList<>();

    private final JPanel upperPanel = new JPanel(new GridLayout(2,3));
    private final JTextField fieldIPAddr = new JTextField("127.0.0.1");
//    private final JTextField fieldIPAddr = new JTextField("89.222.249.131");
    private final JTextField fieldPort = new JTextField("8189");
    private final JCheckBox chkAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField fieldLogin = new JTextField("agcheb");
    private final JPasswordField fieldPass = new JPasswordField("keramika");
    private final JButton btnLogin = new JButton("Login");

    private final JTextArea log = new JTextArea();
    private final JList<String> userList = new JList<String>(chatclients);

    private final JPanel bottomPanel = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("Disconnect");
    private final JTextField fieldInput = new JTextField();
    private final JButton btnSend = new JButton("Send");

 private ChatClientGUI(){
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setTitle(TITLE);
        setLocationRelativeTo(null);

        fieldPass.addActionListener(this);
        fieldPort.addActionListener(this);
        fieldIPAddr.addActionListener(this);
        fieldLogin.addActionListener(this);
        btnLogin.addActionListener(this);
        chkAlwaysOnTop.addActionListener(this);

        btnDisconnect.addActionListener(this);

        btnSend.addActionListener(this);
        fieldInput.addActionListener(this);


        upperPanel.add(fieldIPAddr);
        upperPanel.add(fieldPort);
        upperPanel.add(chkAlwaysOnTop);
        upperPanel.add(fieldLogin);
        upperPanel.add(fieldPass);
        upperPanel.add(btnLogin);
        add(upperPanel, BorderLayout.NORTH);

        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog, BorderLayout.CENTER);




        JScrollPane scrollUsers = new JScrollPane(userList);
        scrollUsers.setPreferredSize(new Dimension(150,0));
        add(scrollUsers, BorderLayout.EAST);

        bottomPanel.add(btnDisconnect, BorderLayout.WEST);
        bottomPanel.add(fieldInput, BorderLayout.CENTER);
        bottomPanel.add(btnSend, BorderLayout.EAST);

        bottomPanel.setVisible(false);
        //bottomPanel.setVisible(true);
        add(bottomPanel, BorderLayout.SOUTH);


        setVisible(true);
 }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == fieldIPAddr ||
                src == fieldLogin ||
                src == fieldPort ||
                src == fieldPass ||
                src ==btnLogin ) {
            connect();
        }
        else if (src == btnDisconnect){
            disconnect();
        }
        else if (src == btnSend || src == fieldInput){
            sendMsg();
        }
        else if (src == chkAlwaysOnTop){
               setAlwaysOnTop(chkAlwaysOnTop.isSelected());
            }

        else{
            throw new RuntimeException("Unknown src = " + src);
        }
    }



    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String msg;
        if(stackTraceElements.length == 0){
            msg = "Пустой stackTraceElements";
        } else {
            msg = e.getClass().getCanonicalName() + ": " + e.getMessage() + "\n" + stackTraceElements[0];
        }
        System.out.println(msg);
        JOptionPane.showMessageDialog(null,msg,"Exception: ", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }


    private SocketThread socketThread;

    private void connect(){
        try {
            Socket socket = new Socket(fieldIPAddr.getText(),Integer.parseInt(fieldPort.getText()));
            socketThread = new SocketThread(this,"SocketThread", socket);
        } catch (IOException e) {
            e.printStackTrace();
            log.append("Exception: " + e.getMessage() + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        }

    }

    private void disconnect(){
        socketThread.close();
        chatclients.removeAllElements(); // Мы отключаемся от чата -> перестаем видеть список участников -> список обнуляется
    }

    private void sendMsg(){
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);

        socketThread.sendMsg(msg);
       // log.append(msg + "\n");
    }


    @Override
    public void onStartSocketThread(SocketThread thread) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    log.append("Поток сокета запущен"+ "\n");
                    log.setCaretPosition(log.getDocument().getLength());
                }
            });
    }

    @Override
    public void onStopSocketThread(SocketThread thread) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append("Соединение потеряно." + "\n");
                log.setCaretPosition(log.getDocument().getLength());
                bottomPanel.setVisible(false);
                upperPanel.setVisible(true);
            }
        });

    }

    @Override
    public void onReadySocketThread(SocketThread thread, Socket socket) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append("Соединение установлено." + "\n");
                log.setCaretPosition(log.getDocument().getLength());
                upperPanel.setVisible(false);
                bottomPanel.setVisible(true);
                String login = fieldLogin.getText();
                String password = new String(fieldPass.getPassword());
                socketThread.sendMsg(Messages.getAuthRequest(login,password));

            }
        });

    }

    @Override
    public void onRecieveString(SocketThread thread, Socket socket, String value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            handleMsg(value);
            }
        });

    }

    private void handleMsg(String msg){

        String[] tokens = msg.split(Messages.DELIMITER);
        if(tokens[0].equals(Messages.AUTH_ACCEPT)){
            log.append("Вы успешно подключились к чату под ником: ");
            log.append(tokens[1]+ "\n");
            log.setCaretPosition(log.getDocument().getLength());

        }
        else if(tokens[0].equals(Messages.AUTH_ERROR)){
            log.append("Не удалось зайти в чат" + "\n");

            log.setCaretPosition(log.getDocument().getLength());
        }
        else if (tokens[0].equals(Messages.USER_LIST)){
            chatclients.addElement(tokens[1]);
        }

        else if(tokens[0].equals(Messages.BROADCAST)){
            if(tokens[2].equals("Server")){
                String[] isconnected = tokens[3].split(" ");
                if (isconnected[1].equals("connected.")) {


                    String msgLog = dateFormat.format(new Date(Long.parseLong(tokens[1]))) + tokens[2] +
                            ": " + tokens[3];
                    log.append(msgLog + "\n");
                    log.setCaretPosition(log.getDocument().getLength());
                    chatclients.addElement(isconnected[0]);
                    //Надо добавить нового клиента в список
                }
                else if(isconnected[1].equals("disconnected.")) {
                    String msgLog = dateFormat.format(new Date(Long.parseLong(tokens[1]))) + tokens[2] +
                            ": " + tokens[3];
                    log.append(msgLog + "\n");
                    log.setCaretPosition(log.getDocument().getLength());
                    chatclients.removeElement(isconnected[0]);
                // Надо удалить клиента из всех списков
                }
            }
            else {

                String msgLog = dateFormat.format(new Date(Long.parseLong(tokens[1]))) + tokens[2] +
                        ": " + tokens[3];
                log.append(msgLog + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        }
        else {
            log.append(Messages.getMsgFormatError("Сообщение неизвестного типа"));
        }


        //log.append("9 deistvitel'no tut!!!!   ");
//        log.append(msg + "\n");
 //       log.setCaretPosition(log.getDocument().getLength());

    }

    @Override
    public void onExceptionSocketThread(SocketThread thread, Socket socket, Exception e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                    e.printStackTrace();
                    log.append("Exception: "+ e.getMessage() + "\n");
                log.setCaretPosition(log.getDocument().getLength());

            }
        });

    }
}



