package com.github.veikkosuhonen.fftapp.fft;

public interface DFT {
    public double[][] process(double[][] dataRI);
    public double[][] process(double[][] dataRI, boolean normalize);
}
