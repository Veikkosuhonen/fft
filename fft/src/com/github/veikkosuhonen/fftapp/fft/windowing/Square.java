package com.github.veikkosuhonen.fftapp.fft.windowing;

/**
 * A no-op window function, which does not modify the signal, when plotted resembles a single square wave
 */
public class Square implements WindowFunction{
    /**
     * Returns the coefficient for a square window function, which is always 1.0
     * @param n The index for which coefficient is to be calculated
     * @return 1.0
     */
    @Override
    public double getCoefficient(int n) {
        return 1.0;
    }
}
