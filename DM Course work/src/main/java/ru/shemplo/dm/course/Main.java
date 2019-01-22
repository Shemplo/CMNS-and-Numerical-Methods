package ru.shemplo.dm.course;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;

public class Main extends Application {

    @FXML
    private Button animationButton;

    @FXML
    private Slider timeSlider;

    @FXML
    private Label timeText;

    @FXML
    private LineChart<Number, Number> xChart;

    @FXML
    private LineChart<Number, Number> tChart;

    @FXML
    private LineChart<Number, Number> wChart;

    private Window window;

    public static void main(String... args) {
        Application.launch(args);
    }

    @FXML
    protected void toggleAnimation(ActionEvent event) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("My portfolio");

        ObservableList<XYChart.Data<Number, Number>> data = series.getData();

        data.add(new XYChart.Data<>(1, 23));
        data.add(new XYChart.Data<>(2, 14));
        data.add(new XYChart.Data<>(3, 15));
        data.add(new XYChart.Data<>(4, 24));
        data.add(new XYChart.Data<>(5, 34));
        data.add(new XYChart.Data<>(6, 36));
        data.add(new XYChart.Data<>(7, 22));
        data.add(new XYChart.Data<>(8, 45));
        data.add(new XYChart.Data<>(9, 43));
        data.add(new XYChart.Data<>(10, 17));
        data.add(new XYChart.Data<>(11, 29));
        data.add(new XYChart.Data<>(12, 25));

        series.setData(data);

        xChart.getData().add(series);

        alert("Registration Successful!", "Welcome");
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL layout = ClassLoader.getSystemResource("fxml/main.fxml");
        Parent root = FXMLLoader.load(layout);
        Scene scene = new Scene(root, 1280, 540);
        window = scene.getWindow();
        stage.setScene(scene);
        stage.show();
    }


    private void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(window);
        alert.show();
    }
}
