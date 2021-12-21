package ru.kpfu.itis.sokolov.model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Game {
    public final int PORT;
    public final InetAddress address;

    public Game(int PORT) throws Exception {
        this.PORT = PORT;
        address = InetAddress.getByName("localhost");
    }

    public void startGame() {
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, address)) {
            System.out.println(address.getHostAddress());
            Socket firstClient = serverSocket.accept();
            Socket secondClient = serverSocket.accept();
            new Thread(new GameThread(firstClient, secondClient, serverSocket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
