package ru.shemplo.dm.course.gfx;


public interface ProgressListener {
    
    public void onComputationStarted ();
    
    public void onStepComputed ();
    
    public void onComputationFinished ();
    
}
