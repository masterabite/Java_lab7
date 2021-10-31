import monitoring.ClientAssistant;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * класс клиента
 */
public class Client {

    public static void main(String[] args){
        try {
            System.out.println("Подключение к серверу...");
            Socket socket = new Socket(InetAddress.getLocalHost(), 4004);
            System.out.println("Клиент запущен");

            ClientAssistant assistant = new ClientAssistant(socket);
            assistant.start();

            socket.close();
        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу.");
        }
    }
}
