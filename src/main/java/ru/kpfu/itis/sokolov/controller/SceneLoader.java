package ru.kpfu.itis.sokolov.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ru.kpfu.itis.sokolov.model.Player;


import java.io.IOException;

public class SceneLoader {

    public static void showWaitingController(Stage primaryStage, int PORT) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("../view/waiting.fxml"));
        Parent root = loader.load();

        WaitingController waitingController = (WaitingController) loader.getController();
        waitingController.init(primaryStage, PORT);

        // show scene
        primaryStage.setTitle("ATARI PONG");
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public static void showGameController(Stage primaryStage, Player player) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("../view/game.fxml"));
        Parent root = loader.load();

        GameController gameController = (GameController) loader.getController();
        gameController.init(primaryStage, player);

        // show scene
        primaryStage.setTitle("ATARI PONG");
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public static void showStartController(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("../view/start.fxml"));
        Parent root = loader.load();

        StartController startController = (StartController) loader.getController();
        startController.init(new Player(), primaryStage);

        // show scene
        primaryStage.setTitle("ATARI PONG");
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public static void showTrainingController(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("../view/training.fxml"));
        Parent root = loader.load();

        TrainingController trainingController = (TrainingController) loader.getController();
        trainingController.init(primaryStage);

        // show scene
        primaryStage.setTitle("ATARI PONG");
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }
}