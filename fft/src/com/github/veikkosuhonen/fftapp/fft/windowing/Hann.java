package com.github.veikkosuhonen.fftapp.fft.windowing;

/**
 * Implementation of the Hann window function ()
 */
public class Hann implements WindowFunction{

    private final int N;

    /**
     * Constructs a new Hann window function instance with window size of {@code N}
     * @param N the size of the window
     */
    public Hann(int N) { this.N = N; }

    /**
     * Calculates the Hann window coefficient for the index
     * @param n The index for which coefficient is to be calculated
     * @return the window coefficient
     */
    @Override
    public double getCoefficient(int n) {
        return 0.5 * (1.0 - Math.cos( 2 * Math.PI * n / N));
    }
}
