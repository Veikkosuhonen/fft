package com.github.veikkosuhonen.fftapp.fft.utils;

public interface ChunkQueue {

    boolean offer(double[] data);

    double[] poll();

    boolean drop(int n);

    int remainingCapacity();

    double[] getWindow(int n);
}
