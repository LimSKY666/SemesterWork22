package ru.kpfu.itis.sokolov.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class TrainingController {
    
    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private double playerOneYPos = height / 2;
    private double playerTwoYPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;
    private int playerOneXPos = 0;
    private double playerTwoXPos = width - PLAYER_WIDTH;

    private Stage primaryStage;
    private Random random = new Random();
    private Timeline tl;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Rectangle playerRectangle;

    @FXML
    private Rectangle opponentRectangle;

    @FXML
    private Circle ball;

    @FXML
    private Text playerPoints;

    @FXML
    private Text opponentPoints;

    @FXML
    private Text result;

    @FXML
    private Text clickForMenuLabel;

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        anchorPane.setOnMouseMoved(e -> {
            playerRectangle.setLayoutY(e.getY());
            playerOneYPos = e.getY();
        });
        tl = new Timeline(new KeyFrame(Duration.millis(10), e -> nextStep()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    private void nextStep() {
        ballXPos+=ballXSpeed;
        ballYPos+=ballYSpeed;
        ball.setCenterY(ballYPos);
        ball.setCenterX(ballXPos);
        if (ballYPos > height || ballYPos < 0) ballYSpeed *= -1;

        opponentRectangle.setLayoutY(ballYPos - PLAYER_HEIGHT/2);
        playerTwoYPos = ballYPos - PLAYER_HEIGHT/2;

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
            opponentPoints.setText("" + scoreP2);
            checkForGameOver();
        }

        if(ballXPos > playerTwoXPos + PLAYER_WIDTH) {
            ballYPos = height/2;
            ballXPos = width/2;
            ballYSpeed = -1;
            ballXSpeed = 1;
            scoreP1++;
            playerPoints.setText("" + scoreP1);
            checkForGameOver();
        }
    }

    private void checkForGameOver() {
        if (scoreP1 == 3) {
            result.setText("You win!");
            clickForMenuLabel.setText("Click for menu");
            endGame();
        }
        if (scoreP2 == 3) {
            result.setText("You lose!");
            clickForMenuLabel.setText("Click for menu");
            endGame();
        }
    }

    private void endGame() {
        tl.stop();
        anchorPane.setOnMouseClicked(e -> {
            try {
                SceneLoader.showStartController(primaryStage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
