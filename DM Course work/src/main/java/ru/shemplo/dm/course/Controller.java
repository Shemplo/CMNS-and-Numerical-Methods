package ru.shemplo.dm.course;

import io.github.egormkn.LatexView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;
import ru.shemplo.dm.course.physics.ProcessorService;
import ru.shemplo.dm.course.physics.methods.ImplicitEulerMethod;
import ru.shemplo.dm.course.physics.methods.Method;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.animation.Animation.Status.RUNNING;

public class Controller implements Initializable {

    private static final StringConverter<Number> converter = new CustomConverter(false);
    private static final StringConverter<Number> texConverter = new CustomConverter(true);

    private final Timeline animationTimeline = new Timeline();

    private ProcessorService service = new ProcessorService();

    private Model model;

    @FXML
    private Button animationButton;

    @FXML
    private Slider sliderTime;

    @FXML
    private LatexView valueTime;

    @FXML
    private Button updateButton;

    @FXML
    private Button resetButton;

    @FXML
    private ChoiceBox<Method> fieldMethod;

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
    private LatexView valueR;

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
        service.cancel();

        model = new Model();

        valueTime.formulaProperty().bind(model.timeProperty().asString("T = %.2f"));

        Bindings.bindBidirectional(fieldStepTime.textProperty(), model.stepTimeProperty(), converter);
        Bindings.bindBidirectional(fieldStepZ.textProperty(), model.stepZProperty(), converter);
        Bindings.bindBidirectional(fieldK.textProperty(), model.kProperty(), converter);
        Bindings.bindBidirectional(fieldE.textProperty(), model.eProperty(), converter);
        Bindings.bindBidirectional(fieldAlpha.textProperty(), model.alphaProperty(), converter);
        Bindings.bindBidirectional(fieldQ.textProperty(), model.qProperty(), converter);
        Bindings.bindBidirectional(fieldT0.textProperty(), model.t0Property(), converter);
        Bindings.bindBidirectional(fieldRho.textProperty(), model.rhoProperty(), converter);
        Bindings.bindBidirectional(fieldC.textProperty(), model.cProperty(), converter);
        Bindings.bindBidirectional(fieldLambda.textProperty(), model.lambdaProperty(), converter);
        Bindings.bindBidirectional(fieldD.textProperty(), model.dProperty(), converter);

        valueDt.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.dtProperty().getValue()),
                model.dtProperty()
        ));
        valueTm.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.tmProperty().getValue()),
                model.tmProperty()
        ));
        valueKappa.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.kappaProperty().getValue()),
                model.kappaProperty()
        ));
        valueR.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.rProperty().getValue()),
                model.rProperty()
        ));
        valueBeta.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.betaProperty().getValue()),
                model.betaProperty()
        ));
        valueGamma.formulaProperty().bind(Bindings.createStringBinding(
                () -> texConverter.toString(model.gammaProperty().getValue()),
                model.gammaProperty()
        ));

        sliderTime.maxProperty().bind(model.maxTimeProperty());
        sliderTime.blockIncrementProperty().bind(model.stepTimeProperty());
        sliderTime.valueProperty().bindBidirectional(model.timeProperty());
        sliderTime.minorTickCountProperty().bind(Bindings.subtract(
                Bindings.divide(1, model.stepTimeProperty()),
                1
        ));

        resetButton.disableProperty().bind(service.runningProperty());
        updateButton.disableProperty().bind(service.runningProperty());

        updateButton.textProperty().bind(Bindings.createStringBinding(
                () -> service.isRunning()
                        ? String.format("%.0f%%", service.getProgress())
                        : "Расчёт",
                service.runningProperty(),
                service.progressProperty()
        ));

        animationButton.textProperty().bind(Bindings.createStringBinding(
                () -> animationTimeline.getStatus() != RUNNING ? "Пуск" : "Пауза",
                animationTimeline.statusProperty()
        ));

        NumberAxis chartXAxisX = (NumberAxis) chartX.getXAxis();
        chartXAxisX.upperBoundProperty().bind(model.maxTimeProperty());

        NumberAxis chartTAxisX = (NumberAxis) chartT.getXAxis();
        chartTAxisX.upperBoundProperty().bind(model.maxTimeProperty());

        NumberAxis chartWAxisX = (NumberAxis) chartW.getXAxis();
        chartWAxisX.upperBoundProperty().bind(model.maxTimeProperty());

        chartX.dataProperty().bind(Bindings.createObjectBinding(
                () -> {
                    int index = (int) Math.round(model.getTime() / model.getStepTime());
                    XYChart.Series<Number, Number> series = model.getDataX().size() > index
                            ? model.getDataX().get(index)
                            : new XYChart.Series<>();
                    return FXCollections.singletonObservableList(series);
                },
                model.timeProperty(),
                model.stepTimeProperty(),
                model.dataXProperty()
        ));

        chartT.dataProperty().bind(Bindings.createObjectBinding(
                () -> {
                    int index = (int) Math.round(model.getTime() / model.getStepTime());
                    XYChart.Series<Number, Number> series = model.getDataT().size() > index
                            ? model.getDataT().get(index)
                            : new XYChart.Series<>();
                    return FXCollections.singletonObservableList(series);
                },
                model.timeProperty(),
                model.stepTimeProperty(),
                model.dataTProperty()
        ));

        chartW.dataProperty().bind(Bindings.createObjectBinding(
                () -> {
                    int index = (int) Math.round(model.getTime() / model.getStepTime());
                    XYChart.Series<Number, Number> series = model.getDataW().size() > index
                            ? model.getDataW().get(index)
                            : new XYChart.Series<>();
                    return FXCollections.singletonObservableList(series);
                },
                model.timeProperty(),
                model.stepTimeProperty(),
                model.dataWProperty()
        ));

        service.setModel(model);
        service.setOnSucceeded(e -> {
            ProcessorResult result = service.getValue();
            model.getDataX().setAll(result.getDataX());
            model.getDataT().setAll(result.getDataT());
            model.getDataW().setAll(result.getDataW());
        });

        update();
    }

    @FXML
    private void update() {
        animationTimeline.stop();
        model.setTime(0);

        System.out.println("Update");

        KeyValue keyValue = new KeyValue(model.timeProperty(), model.getMaxTime());
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10000), event -> model.setTime(0), keyValue);
        animationTimeline.getKeyFrames().setAll(keyFrame);

        service.restart();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldMethod.getItems().addAll(new ImplicitEulerMethod(), new ImplicitEulerMethod());
        fieldMethod.getSelectionModel().selectFirst();

        reset();
    }

    private static class CustomConverter extends NumberStringConverter {

        private final boolean tex;

        public CustomConverter(boolean tex) {
            super();
            this.tex = tex;
        }

        @Override
        public Number fromString(String value) {
            return Double.parseDouble(value.trim());
        }

        @Override
        public String toString(Number value) {
            String result = String.valueOf(value).replaceFirst("(\\.\\d{6})\\d+", "$1");
            return !tex ? result : result.replaceFirst("E(-?\\d+)", "*10^{$1}");
        }
    }
}
