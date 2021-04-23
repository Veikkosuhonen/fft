package com.github.veikkosuhonen.fftapp.fft.windowing;

public class Square implements WindowFunction{
    @Override
    public double getCoefficient(int n) {
        return 1.0;
    }
}
