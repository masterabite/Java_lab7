import monitoring.ServerAssistant;

import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Класс сервера
 */
public class Server {
    private static Selector selector;
    private static ServerSocketChannel serverChannel;

    public static void main(String[] args){
        try {
            serverChannel = ServerSocketChannel.open();
            selector = Selector.open();
            InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(),4004);
            serverChannel.configureBlocking(false);
            serverChannel.bind(address);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Сервер запущен.");
            ServerAssistant assistant = new ServerAssistant(args, serverChannel, selector);
            assistant.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
