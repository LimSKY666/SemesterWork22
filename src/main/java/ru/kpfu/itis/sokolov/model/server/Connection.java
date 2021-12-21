package ru.kpfu.itis.sokolov.model.server;

import ru.kpfu.itis.sokolov.model.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
    int PORT;
    Player player;

    public Connection(int PORT, Player player) {
        this.PORT = PORT;
        this.player = player;
    }

    public boolean connectToServer() {
        try {
            System.out.println(PORT);
            Socket socket = new Socket("localhost", PORT);
            player.scanner = new Scanner(socket.getInputStream());
            player.writer = new PrintWriter(socket.getOutputStream(), true);
            player.socket = socket;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
