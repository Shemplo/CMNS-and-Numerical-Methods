package ru.shemplo.dm.course;

import io.github.egormkn.LatexView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;
import ru.shemplo.dm.course.physics.ProcessorService;
import ru.shemplo.dm.course.physics.schemes.Scheme;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.animation.Animation.Status.RUNNING;

public class Controller implements Initializable {

    private static final StringConverter<Number> converter = new CustomConverter(false);
    private static final StringConverter<Number> texConverter = new CustomConverter(true);

    private final Timeline animationTimeline = new Timeline();

    private Model model;

    private Service<ProcessorResult> service;

    @FXML
    private Button animationButton;

    @FXML
    private Slider sliderTime;

    @FXML
    private LatexView valueTime;

    @FXML
    private HBox animationPanel;

    @FXML
    private HBox progressPanel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private VBox sidebar;

    @FXML
    private ChoiceBox<Scheme.Type> fieldMethod;

    @FXML
    private TextField fieldStepTime;

    @FXML
    private TextField fieldMaxTime;

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
    private LatexView valueStepCoord;

    @FXML
    private LatexView valueMaxCoord;

    @FXML
    private LatexView valueDt;

    @FXML
    private LatexView valueTm;

    @FXML
    private LatexView valueKappa;

    @FXML
    private LatexView valueR;

    @FXML
    private LatexView valueBeta;

    @FXML
    private LatexView valueGamma;

    @FXML
    private LatexView valueActivated;

    @FXML
    private LatexView valueU;

    @FXML
    private LatexView valueDeltaH;

    @FXML
    private LatexView valueDeltaR;

    @FXML
    private LatexView valueDeltaD;

    @FXML
    private LatexView valueLe;

    @FXML
    private LineChart<Number, Number> chartX;

    @FXML
    private LineChart<Number, Number> chartT;

    @FXML
    private LineChart<Number, Number> chartW;

    private XYChart.Series<Number, Number> seriesX = new XYChart.Series<>();

    private XYChart.Series<Number, Number> seriesT = new XYChart.Series<>();

    private XYChart.Series<Number, Number> seriesW = new XYChart.Series<>();

    @FXML
    private void toggleAnimation() {
        if (animationTimeline.getStatus() == RUNNING) {
            animationTimeline.pause();
        } else {
            animationTimeline.play();
        }
    }

    @FXML
    private void reset() {
        stopService();

        model = new Model();
        service = new ProcessorService(model);

        valueTime.formulaProperty().bind(model.timeProperty().asString("T = %.2f"));

        model.processorProperty().bind(fieldMethod.valueProperty());

        Bindings.bindBidirectional(fieldStepTime.textProperty(), model.stepTimeProperty(), converter);
        Bindings.bindBidirectional(fieldMaxTime.textProperty(), model.maxTimeProperty(), converter);
        Bindings.bindBidirectional(fieldK.textProperty(), model.kProperty(), converter);
        Bindings.bindBidirectional(fieldE.textProperty(), model.eProperty(), converter);
        Bindings.bindBidirectional(fieldAlpha.textProperty(), model.alphaProperty(), converter);
        Bindings.bindBidirectional(fieldQ.textProperty(), model.qProperty(), converter);
        Bindings.bindBidirectional(fieldT0.textProperty(), model.t0Property(), converter);
        Bindings.bindBidirectional(fieldRho.textProperty(), model.rhoProperty(), converter);
        Bindings.bindBidirectional(fieldC.textProperty(), model.cProperty(), converter);
        Bindings.bindBidirectional(fieldLambda.textProperty(), model.lambdaProperty(), converter);
        Bindings.bindBidirectional(fieldD.textProperty(), model.dProperty(), converter);

        valueStepCoord.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getStepCoord()),
                model.stepCoordProperty()
        ));
        valueMaxCoord.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getMaxCoord()),
                model.maxCoordProperty()
        ));
        valueDt.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getDt()),
                model.dtProperty()
        ));
        valueTm.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getTm()),
                model.tmProperty()
        ));
        valueKappa.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getKappa()),
                model.kappaProperty()
        ));
        valueR.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getR()),
                model.rProperty()
        ));
        valueBeta.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getBeta()),
                model.betaProperty()
        ));
        valueGamma.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getGamma()),
                model.gammaProperty()
        ));
        valueActivated.formulaProperty().bind(Bindings.createStringBinding(
                () -> model.isActivated()
                        ? "\\textcolor{#006600}\\text{Реакция активирована}"
                        : "\\textcolor{#FF0000}\\text{Реакция не активирована}",
                model.activatedProperty()
        ));
        valueU.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getU()),
                model.uProperty()
        ));
        valueDeltaH.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getDeltaH()),
                model.deltaHProperty()
        ));
        valueDeltaR.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getDeltaR()),
                model.deltaRProperty()
        ));
        valueDeltaD.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getDeltaD()),
                model.deltaDProperty()
        ));
        valueLe.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.getLe()),
                model.leProperty()
        ));


        sliderTime.maxProperty().bind(model.maxTimeProperty());
        sliderTime.blockIncrementProperty().bind(model.stepTimeProperty());
        sliderTime.valueProperty().bindBidirectional(model.timeProperty());
        sliderTime.minorTickCountProperty().bind(Bindings.subtract(
                Bindings.divide(1, model.stepTimeProperty()), 1
        ));

        sidebar.disableProperty().bind(service.runningProperty());
        animationPanel.disableProperty().bind(service.runningProperty().or(service.valueProperty().isNull()));

        animationPanel.visibleProperty().bind(Bindings.not(service.runningProperty()));
        progressPanel.visibleProperty().bind(service.runningProperty());

        progressBar.progressProperty().bind(service.progressProperty());

        animationButton.textProperty().bind(Bindings.createStringBinding(
                () -> animationTimeline.getStatus() != RUNNING ? "Пуск" : "Пауза",
                animationTimeline.statusProperty()
        ));

        NumberAxis chartXAxisX = (NumberAxis) chartX.getXAxis();
        chartXAxisX.upperBoundProperty().bind(model.maxCoordProperty());
        chartXAxisX.tickUnitProperty().bind(model.deltaHProperty());

        NumberAxis chartTAxisX = (NumberAxis) chartT.getXAxis();
        chartTAxisX.upperBoundProperty().bind(model.maxCoordProperty());
        chartTAxisX.tickUnitProperty().bind(model.deltaHProperty());

        NumberAxis chartWAxisX = (NumberAxis) chartW.getXAxis();
        chartWAxisX.upperBoundProperty().bind(model.maxCoordProperty());
        chartWAxisX.tickUnitProperty().bind(model.deltaHProperty());

        seriesX.dataProperty().bind(Bindings.createObjectBinding(
                () -> {
                    int index = (int) Math.round(model.getTime() / model.getStepTime());
                    ObservableList<double[]> dataX = model.getDataX();
                    if (dataX.size() > index) {
                        double[] values = dataX.get(index);
                        ObservableList<XYChart.Data<Number, Number>> result = FXCollections.observableArrayList();
                        for (int i = 0; i < values.length; i++) {
                            result.add(new XYChart.Data<>(i * model.getStepCoord(), values[i]));
                        }
                        return result;
                    } else {
                        return FXCollections.emptyObservableList();
                    }
                },
                model.timeProperty(),
                model.stepTimeProperty(),
                model.stepCoordProperty(),
                model.dataXProperty()
        ));

        seriesT.dataProperty().bind(Bindings.createObjectBinding(
                () -> {
                    int index = (int) Math.round(model.getTime() / model.getStepTime());
                    ObservableList<double[]> dataT = model.getDataT();
                    if (dataT.size() > index) {
                        double[] values = dataT.get(index);
                        ObservableList<XYChart.Data<Number, Number>> result = FXCollections.observableArrayList();
                        for (int i = 0; i < values.length; i++) {
                            result.add(new XYChart.Data<>(i * model.getStepCoord(), values[i]));
                        }
                        return result;
                    } else {
                        return FXCollections.emptyObservableList();
                    }
                },
                model.timeProperty(),
                model.stepTimeProperty(),
                model.stepCoordProperty(),
                model.dataTProperty()
        ));

        seriesW.dataProperty().bind(Bindings.createObjectBinding(
                () -> {
                    int index = (int) Math.round(model.getTime() / model.getStepTime());
                    ObservableList<double[]> dataW = model.getDataW();
                    if (dataW.size() > index) {
                        double[] values = dataW.get(index);
                        ObservableList<XYChart.Data<Number, Number>> result = FXCollections.observableArrayList();
                        for (int i = 0; i < values.length; i++) {
                            result.add(new XYChart.Data<>(i * model.getStepCoord(), values[i]));
                        }
                        return result;
                    } else {
                        return FXCollections.emptyObservableList();
                    }
                },
                model.timeProperty(),
                model.stepTimeProperty(),
                model.stepCoordProperty(),
                model.dataWProperty()
        ));

        service.setOnSucceeded(e -> {
            ProcessorResult result = service.getValue();
            model.getDataX().setAll(result.getDataX());
            model.getDataT().setAll(result.getDataT());
            model.getDataW().setAll(result.getDataW());

            chartX.getData().setAll(Collections.singleton(seriesX));
            chartT.getData().setAll(Collections.singleton(seriesT));
            chartW.getData().setAll(Collections.singleton(seriesW));


            ObservableList<XYChart.Data<Number, Number>> trendList = FXCollections.observableArrayList();
            for (int z = 0; z < result.getTrendW().length; z++) {
                trendList.add(new XYChart.Data<>(z * model.getStepCoord(), result.getTrendW()[z]));
            }
            XYChart.Series<Number, Number> trend = new XYChart.Series<>(trendList);
            chartW.getData().add(trend);
            trend.getNode().setStyle("-fx-stroke-dash-array: 3 3;-fx-stroke: #000000;");

            double ticks = result.getDataX().size();

            for (int i = 0; i < ticks; i += ticks / 20) {
                double[] values = result.getDataX().get(i);
                ObservableList<XYChart.Data<Number, Number>> list = FXCollections.observableArrayList();
                for (int j = 0; j < values.length; j++) {
                    list.add(new XYChart.Data<>(j * model.getStepCoord(), values[j]));
                }
                XYChart.Series<Number, Number> series = new XYChart.Series<>(list);
                chartX.getData().add(series);
            }

            for (int i = 0; i < ticks; i += ticks / 20) {
                double[] values = result.getDataT().get(i);
                ObservableList<XYChart.Data<Number, Number>> list = FXCollections.observableArrayList();
                for (int j = 0; j < values.length; j++) {
                    list.add(new XYChart.Data<>(j * model.getStepCoord(), values[j]));
                }
                XYChart.Series<Number, Number> series = new XYChart.Series<>(list);
                chartT.getData().add(series);
            }

            for (int i = 0; i < ticks; i += ticks / 20) {
                double[] values = result.getDataW().get(i);
                ObservableList<XYChart.Data<Number, Number>> list = FXCollections.observableArrayList();
                for (int j = 0; j < values.length; j++) {
                    list.add(new XYChart.Data<>(j * model.getStepCoord(), values[j]));
                }
                XYChart.Series<Number, Number> series = new XYChart.Series<>(list);
                chartW.getData().add(series);
                //series.getNode().setStyle("-fx-stroke-dash-array: 5 5;");
            }

            ProcessorResult.Bounds boundsX = result.getBoundsX();
            NumberAxis chartXAxisY = (NumberAxis) chartX.getYAxis();
            chartXAxisY.setLowerBound(boundsX.getMin());
            chartXAxisY.setUpperBound(boundsX.getMax());
            chartXAxisY.setTickUnit((boundsX.getMax() - boundsX.getMin()) / 10);

            ProcessorResult.Bounds boundsT = result.getBoundsT();
            NumberAxis chartTAxisY = (NumberAxis) chartT.getYAxis();
            chartTAxisY.setLowerBound(boundsT.getMin());
            chartTAxisY.setUpperBound(boundsT.getMax());
            chartTAxisY.setTickUnit((boundsT.getMax() - boundsT.getMin()) / 10);

            ProcessorResult.Bounds boundsW = result.getBoundsW();
            NumberAxis chartWAxisY = (NumberAxis) chartW.getYAxis();
            chartWAxisY.setLowerBound(boundsW.getMin());
            chartWAxisY.setUpperBound(boundsW.getMax());
            chartWAxisY.setTickUnit((boundsW.getMax() - boundsW.getMin()) / 10);

            KeyValue keyValue = new KeyValue(model.timeProperty(), model.getMaxTime());
            KeyFrame keyFrame = new KeyFrame(Duration.millis(10000), event -> model.setTime(0), keyValue);
            animationTimeline.getKeyFrames().setAll(keyFrame);

            System.out.println("Ready");
        });
    }

    @FXML
    private void update() {
        animationTimeline.stop();
        model.setTime(0);
        model.getDataX().clear();
        model.getDataT().clear();
        model.getDataW().clear();
        chartX.getData().clear();
        chartT.getData().clear();
        chartW.getData().clear();

        service.restart();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldMethod.getItems().addAll(Scheme.Type.values());
        fieldMethod.getSelectionModel().selectFirst();

        reset();
    }

    @FXML
    public void stopService() {
        if (service != null) {
            service.cancel();
        }
    }

    private static class CustomConverter extends NumberStringConverter {

        private final boolean tex;

        public CustomConverter(boolean tex) {
            super();
            this.tex = tex;
        }

        @Override
        public Number fromString(String value) {
            try {
                return Double.parseDouble(value.trim());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }

        @Override
        public String toString(Number value) {
            String result = String.valueOf(value).replaceFirst("(\\.\\d{6})\\d+", "$1");
            return !tex ? result : result.replaceFirst("E(-?\\d+)", "*10^{$1}");
        }
    }
}
