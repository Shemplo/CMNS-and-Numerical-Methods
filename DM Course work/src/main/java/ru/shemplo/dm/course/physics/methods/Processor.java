package ru.shemplo.dm.course.physics.methods;

import javafx.concurrent.Task;
import ru.shemplo.dm.course.physics.Model;
import ru.shemplo.dm.course.physics.ProcessorResult;

import java.util.function.Function;

public abstract class Processor extends Task<ProcessorResult> {

    protected final Model model;

    public Processor(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public enum Type {
        DEFAULT("Процессор Андрея", DefaultProcessor::new),
        RANDOM("Случайные значения", RandomProcessor::new);

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
