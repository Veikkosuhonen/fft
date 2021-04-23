package com.github.veikkosuhonen.fftapp.fft.windowing;

public class Hann implements WindowFunction{

    private final int N;
    public Hann(int N) { this.N = N; }

    @Override
    public double getCoefficient(int n) {
        return 0.5 * (1.0 - Math.cos( 2 * Math.PI * n / N));
    }
}
