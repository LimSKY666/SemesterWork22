package ru.kpfu.itis.sokolov.model.server;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class GameThread implements Runnable {
    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 10;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private Double playerOneYPos = null;
    private Double playerTwoYPos= null;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private int playerOneXPos = 0;
    private double playerTwoXPos = width - PLAYER_WIDTH;
    private Timeline tl;
    private Socket firstClient;
    private Socket secondClient;

    private Scanner scannerFromFirstClient;
    private PrintWriter writerToFirstClient;
    private Scanner scannerFromSecondClient;
    private PrintWriter writerToSecondClient;
    private ServerSocket currentServer;
    private Thread firstPlayerListener = new Thread(new FirstPlayerListener());
    private Thread secondPlayerListener = new Thread(new SecondPlayerListener());
    private Random random = new Random();

    public GameThread(Socket firstClient, Socket secondClient, ServerSocket currentServer) throws IOException {
        this.currentServer = currentServer;

        scannerFromFirstClient = new Scanner(firstClient.getInputStream());
        writerToFirstClient = new PrintWriter(firstClient.getOutputStream(), true);

        scannerFromSecondClient = new Scanner(secondClient.getInputStream());
        writerToSecondClient = new PrintWriter(secondClient.getOutputStream(), true);

        this.firstClient = firstClient;
        this.secondClient = secondClient;
    }

    @Override
    public void run() {
        writerToFirstClient.println("gameStarted");
        writerToSecondClient.println("gameStarted");

        firstPlayerListener.start();
        secondPlayerListener.start();
        tl = new Timeline(new KeyFrame(Duration.millis(10), e -> nextStep()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    private class FirstPlayerListener implements Runnable {

        @Override
        public void run() {
            while(scannerFromFirstClient.hasNext()){
                playerOneYPos = Double.parseDouble(scannerFromFirstClient.nextLine());
                writerToSecondClient.println("oy:" + playerOneYPos);
            }
        }
    }

    private class SecondPlayerListener implements Runnable {

        @Override
        public void run() {
            while(scannerFromSecondClient.hasNext()){
                playerTwoYPos = Double.parseDouble(scannerFromSecondClient.nextLine());
                writerToFirstClient.println("oy:" + playerTwoYPos);
            }
        }
    }

    private void nextStep() {
        ballXPos+=ballXSpeed;
        ballYPos+=ballYSpeed;
        if (ballYPos > height || ballYPos < 0) ballYSpeed *= -1;
        writerToFirstClient.println("bx:" + ballXPos);
        writerToSecondClient.println("bx:" + (width-ballXPos));
        writerToFirstClient.println("by:" + ballYPos);
        writerToSecondClient.println("by:" + ballYPos);

        if( ((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) ||
                ((ballXPos < playerOneXPos + PLAYER_WIDTH + BALL_R) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
            ballYSpeed += (random.nextInt(2) + 1) * Math.signum(ballYSpeed);
            ballXSpeed += (random.nextInt(2) + 1) * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        if(ballXPos < playerOneXPos - PLAYER_WIDTH) {
            ballYPos = height/2;
            ballXPos = width/2;
            ballYSpeed = 1;
            ballXSpeed = 1;
            scoreP2++;
            writerToFirstClient.println("op:" + scoreP2);
            writerToSecondClient.println("pp:" + scoreP2);
            checkForGameOver();
        }

        if(ballXPos > playerTwoXPos + PLAYER_WIDTH) {
            ballYPos = height/2;
            ballXPos = width/2;
            ballYSpeed = -1;
            ballXSpeed = 1;
            scoreP1++;
            writerToFirstClient.println("pp:" + scoreP1);
            writerToSecondClient.println("op:" + scoreP1);
            checkForGameOver();
        }
    }

    private void checkForGameOver() {
        if (scoreP1 == 3) {
            writerToFirstClient.println("go:You win!");
            writerToSecondClient.println("go:You lose!");
            endGame();
        }
        if (scoreP2 == 3) {
            writerToSecondClient.println("go:You win!");
            writerToFirstClient.println("go:You lose!");
            endGame();
        }
    }

    private void endGame() {
        try {
            tl.stop();
            currentServer.close();
            firstPlayerListener.interrupt();
            secondPlayerListener.interrupt();
            firstClient.close();
            secondClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
