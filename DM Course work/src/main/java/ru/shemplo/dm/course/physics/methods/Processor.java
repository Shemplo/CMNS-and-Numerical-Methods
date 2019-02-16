package ru.shemplo.dm.course.physics.methods;

import javafx.concurrent.Task;
import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class Processor extends Task<ProcessorResult> {

    protected final Model model;

    protected final int ticks, coords;

    public Processor(Model model) {
        this.model = model;
        this.ticks = (int) (Math.ceil(model.getMaxTime() / model.getStepTime()) + 1);
        this.coords = (int) (Math.ceil(model.getMaxCoord() / model.getStepCoord()) + 1);
        System.out.println("Task size (t * z): " + ticks + " * " + coords);
    }

    public Model getModel() {
        return model;
    }

    public int getTicks() {
        return ticks;
    }

    public int getCoords() {
        return coords;
    }

    @Override
    protected ProcessorResult call() {
        List<double[]>
                dataX = new ArrayList<>(),
                dataT = new ArrayList<>(),
                dataW = new ArrayList<>();

        try {
            double[] stepX = getInitialStepX();
            double[] stepT = getInitialStepT();

            for (int time = 0; time < ticks && !isCancelled(); time++) {
                double[] stepW = new double[coords];
                for (int i = 0; i < coords; i++) {
                    stepW[i] = model.getW(stepX[i], stepT[i]);
                }

                if (time % (ticks / 30) == 0) {
                    System.out.println(Arrays.toString(stepX));
                }

                dataX.add(stepX);
                dataT.add(stepT);
                dataW.add(stepW);

                double[] newStepX = getStepX(stepX, stepT);
                double[] newStepT = getStepT(stepX, stepT);

                stepX = newStepX;
                stepT = newStepT;

                updateProgress(time, ticks);
            }

            if (isCancelled()) {
                System.out.println("Task cancelled");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ProcessorResult(dataX, dataT, dataW);
    }

    public abstract double[] getStepX(double[] oldX, double[] oldT);

    public abstract double[] getStepT(double[] oldX, double[] oldT);

    protected double[] getInitialStepX() {
        double[] solutionX = new double[coords];
        Arrays.fill(solutionX, 1);
        solutionX[0] = 0;
        return solutionX;
    }

    protected double[] getInitialStepT() {
        double[] solutionT = new double[coords];
        Arrays.fill(solutionT, model.getT0());
        solutionT[0] = model.getTm();
        return solutionT;
    }

    public enum Type {
        DEFAULT("Процессор Андрея", DefaultProcessor::new),
        EXPLICIT("Explicit процессор", ExplicitProcessor::new),
        IMPLICIT_LAT("Implicit процессор Латышева", ImplicitLatyshevProcessor::new);

        private final String name;
        private final Function<Model, Task<ProcessorResult>> constructor;

        Type(String name, Function<Model, Task<ProcessorResult>> constructor) {
            this.name = name;
            this.constructor = constructor;
        }

        @Override
        public String toString() {
            return name;
        }

        public Task<ProcessorResult> build(Model model) {
            return constructor.apply(model);
        }
    }
}
