package ru.shemplo.dm.course.gfx;


public interface ProgressListener {

    void onComputationStarted();

    void onStepComputed();

    void onComputationFinished();

}
