package ru.geekbrains.chat.server.simples;

/**
 * Created by Александр on 03.07.2017.
 */
public class Main {

    public static void main(String[] args){

        try {
            Class.forName("ru.geekbrains.chat.server.simples.ExampleClass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
