package ru.kpfu.itis.sokolov.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.kpfu.itis.sokolov.model.Player;

import java.io.IOException;

public class GameController {
    Player player;
    Stage primaryStage;
    Thread serverListener = new Thread(new ServerListener());

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

    private void setOpponentPositionY(double opponentPositionY) {
        opponentRectangle.setLayoutY(opponentPositionY);
    }

    private void setBallPositionX(double positionX) {
        ball.setCenterX(positionX);
    }

    private void setBallPositionY(double positionY) {
        ball.setCenterY(positionY);
    }

    private void setPlayerPoints(int value) {
        playerPoints.setText("" + value);
    }

    private void setOpponentPoints(int value) {
        opponentPoints.setText("" + value);
    }

    private void setResult(String result) {
        this.result.setText(result);
        clickForMenuLabel.setText("Click for menu");
        anchorPane.setOnMouseMoved(null);
        anchorPane.setOnMouseClicked(e -> {
            try {
                SceneLoader.showStartController(primaryStage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        serverListener.interrupt();
        player = null;
    }

    public void init(Stage primaryStage, Player player) {
        this.primaryStage = primaryStage;
        this.player = player;
        anchorPane.setOnMouseMoved(e -> {
            playerRectangle.setLayoutY(e.getY());
            sendPositionToSever(e.getY());
        });
        player.writer.println(250);
        serverListener.start();
    }

    private void sendPositionToSever(double y) {
        player.writer.println(y);
    }

    private class ServerListener implements Runnable {

        @Override
        public void run() {
            while(player.scanner.hasNext()) {
                String answer = player.scanner.nextLine();
                if (answer.startsWith("bx")) {
                    String ballX = answer.split(":")[1];
                    Platform.runLater(() -> setBallPositionX(Double.parseDouble(ballX)));
                } else if (answer.startsWith("by")) {
                    String ballY = answer.split(":")[1];
                    Platform.runLater(() -> setBallPositionY(Double.parseDouble(ballY)));
                } else if (answer.startsWith("oy")) {
                    String opponentY = answer.split(":")[1];
                    Platform.runLater(() -> setOpponentPositionY(Double.parseDouble(opponentY)));
                } else if (answer.startsWith("pp")) {
                    String playerPoints = answer.split(":")[1];
                    Platform.runLater(() -> setPlayerPoints(Integer.parseInt(playerPoints)));
                } else if (answer.startsWith("op")) {
                    String opponentPoints = answer.split(":")[1];
                    Platform.runLater(() -> setOpponentPoints(Integer.parseInt(opponentPoints)));
                } else if (answer.startsWith("go")) {
                    String result = answer.split(":")[1];
                    Platform.runLater(() -> setResult(result));
                }
            }
        }
    }

}
