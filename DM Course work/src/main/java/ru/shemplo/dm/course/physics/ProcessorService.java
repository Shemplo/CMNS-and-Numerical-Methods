package ru.shemplo.dm.course.physics;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ProcessorService extends Service<ProcessorResult> {

    private final Model model;

    public ProcessorService(Model model) {
        this.model = model;
    }

    @Override
    protected Task<ProcessorResult> createTask() {
        return model.getProcessor().build(model);
    }
}
