package ru.shemplo.dm.course.gfx;

/**
 * Created by Анастасия on 31.01.2019.
 */
public class ComputationProgressListener implements ProgressListener {

    @Override
    public void onComputationStarted() {
        System.out.println("Computation started");
    }

    @Override
    public void onStepComputed() {
        System.out.println("Step computed");
    }

    @Override
    public void onComputationFinished() {
        System.out.println("Computation finished");
    }
}