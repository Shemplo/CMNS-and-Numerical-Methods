package ru.shemplo.dm.course.physics.methods;

import javafx.concurrent.Task;
import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;

import java.util.function.Function;

public abstract class Processor extends Task<ProcessorResult> {

    protected final Model model;

    protected final int ticks, coords;

    public Processor(Model model) {
        this.model = model;
        this.ticks = (int) (Math.ceil(model.getMaxTime() / model.getStepTime()) + 1);
        this.coords = (int) (Math.ceil(model.getMaxCoord() / model.getStepCoord()) + 1);
        System.out.println("Size: " + ticks + " x " + coords);
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

    public enum Type {
        DEFAULT("Процессор Андрея", DefaultProcessor::new),
        RANDOM("Случайные значения", RandomProcessor::new),
        EXPLICIT("Explicit процессор Латышева", ExplicitLatyshevProcessor::new),
        IMPLICIT("Implicit процессор Латышева", SomeLatyshevProcessor::new),
        IMPLICIT_PLUS("Implicit+ процессор Латышева", StupidLatyshevProcessor::new);

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

        public Function<Model, Task<ProcessorResult>> getConstructor() {
            return constructor;
        }

        public Task<ProcessorResult> build(Model model) {
            return constructor.apply(model);
        }
    }
}
