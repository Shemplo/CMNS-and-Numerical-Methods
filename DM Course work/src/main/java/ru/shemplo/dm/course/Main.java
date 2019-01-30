package ru.shemplo.dm.course;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL layout = Main.class.getResource("/fxml/layout.fxml");
        Parent root = FXMLLoader.load(layout);
        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }
}
