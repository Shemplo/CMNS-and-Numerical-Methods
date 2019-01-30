package ru.shemplo.dm.course;

import io.github.egormkn.LatexView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Duration;
import ru.shemplo.dm.course.physics.Model;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Model model;

    @FXML
    private Button animationButton;

    @FXML
    private Slider sliderTime;

    @FXML
    private LatexView valueTime;

    @FXML
    private ChoiceBox<String> fieldMethod;

    @FXML
    private TextField fieldStepTime;

    @FXML
    private TextField fieldStepZ;

    @FXML
    private TextField fieldK;

    @FXML
    private TextField fieldE;

    @FXML
    private TextField fieldAlpha;

    @FXML
    private TextField fieldQ;

    @FXML
    private TextField fieldT0;

    @FXML
    private TextField fieldRho;

    @FXML
    private TextField fieldC;

    @FXML
    private TextField fieldLambda;

    @FXML
    private TextField fieldD;

    @FXML
    private LatexView valueDt;

    @FXML
    private LatexView valueTm;

    @FXML
    private LatexView valueKappa;

    @FXML
    private LatexView valueBeta;

    @FXML
    private LatexView valueGamma;

    @FXML
    private LineChart<Number, Number> chartX;

    @FXML
    private LineChart<Number, Number> chartT;

    @FXML
    private LineChart<Number, Number> chartW;

    private Timeline animationTimeline = new Timeline();

    private static String format(double value) {
        return String.valueOf(value).replaceFirst("(\\.\\d{6})\\d+", "$1");
    }

    private static String formatTex(double value) {
        return format(value).replaceFirst("E(-?\\d+)", "*10^{$1}");
    }

    @FXML
    private void toggleAnimation(ActionEvent event) {
        switch (animationTimeline.getStatus()) {
            case STOPPED:
                sliderTime.setValue(0);
            case PAUSED:
                animationTimeline.play();
                animationButton.setText("Пауза");
                break;
            case RUNNING:
                animationTimeline.pause();
                animationButton.setText("Пуск");
                break;
        }
    }

    @FXML
    private void reset(ActionEvent event) {
        model = new Model();

        fieldStepTime.setText(String.valueOf(model.getStepTime()));
        fieldStepZ.setText(String.valueOf(model.getStepZ()));
        fieldK.setText(String.valueOf(model.getK()));
        fieldE.setText(String.valueOf(model.getE()));
        fieldAlpha.setText(String.valueOf(model.getAlpha()));
        fieldQ.setText(String.valueOf(model.getQ()));
        fieldT0.setText(String.valueOf(model.getT0()));
        fieldRho.setText(String.valueOf(model.getRho()));
        fieldC.setText(String.valueOf(model.getC()));
        fieldLambda.setText(String.valueOf(model.getLambda()));
        fieldD.setText(String.valueOf(model.getD()));

        update(null);
    }

    private void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Header");
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void update(ActionEvent actionEvent) {
        animationTimeline.stop();

        sliderTime.setMin(0);
        sliderTime.setMax(model.getMaxTime());
        sliderTime.setValue(0);

        animationButton.setText("Пуск");
        KeyValue keyValue = new KeyValue(sliderTime.valueProperty(), model.getMaxTime());
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10000), keyValue);
        animationTimeline = new Timeline(keyFrame);
        //animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.setOnFinished(event -> {
            animationButton.setText("Пуск");
            sliderTime.setValue(0);
        });

        model.setStepTime(Double.parseDouble(fieldStepTime.getText()));
        model.setStepZ(Double.parseDouble(fieldStepZ.getText()));
        model.setK(Double.parseDouble(fieldK.getText()));
        model.setE(Double.parseDouble(fieldE.getText()));
        model.setAlpha(Double.parseDouble(fieldAlpha.getText()));
        model.setQ(Double.parseDouble(fieldQ.getText()));
        model.setT0(Double.parseDouble(fieldT0.getText()));
        model.setRho(Double.parseDouble(fieldRho.getText()));
        model.setC(Double.parseDouble(fieldC.getText()));
        model.setLambda(Double.parseDouble(fieldLambda.getText()));
        model.setD(Double.parseDouble(fieldD.getText()));

        valueDt.setFormula(formatTex(model.getDt()));
        valueTm.setFormula(formatTex(model.getTm()));
        valueKappa.setFormula(formatTex(model.getKappa()));
        valueBeta.setFormula(formatTex(model.getBeta()));
        valueGamma.setFormula(formatTex(model.getGamma()));

        // TODO: Calculate X, T, W
        draw();
    }

    private void draw() {
        // Chart demo
        // TODO: Implement charts using Observables
        // TODO: Bind charts time to sliderTime.valueProperty() (see initialize method)
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<Number, Number>> data = series.getData();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            data.add(new XYChart.Data<>(i, i * random.nextInt(20)));
        }
        series.setData(data);
        chartX.getData().clear();
        chartX.getData().add(series);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldMethod.getItems().addAll("Метод 1", "Метод 2", "Метод 3");
        fieldMethod.setValue("Метод 1");
        valueTime.formulaProperty().bind(sliderTime.valueProperty().asString("T = %.0f"));

        reset(null);
    }
}
