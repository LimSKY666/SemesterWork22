package ru.kpfu.itis.sokolov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kpfu.itis.sokolov.controller.StartController;
import ru.kpfu.itis.sokolov.model.Player;

public class Main extends Application {
    Player player;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/start.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("ATARI PONG");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        StartController startController = (StartController) loader.getController();
        startController.init(player, primaryStage);

        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        player = new Player();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

