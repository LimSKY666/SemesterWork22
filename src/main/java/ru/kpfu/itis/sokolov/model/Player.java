package ru.kpfu.itis.sokolov.model;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player {

    public Scanner scanner;
    public PrintWriter writer;
    public Socket socket;

    public boolean waitOpponent() {
        return scanner.hasNext();
    }
}