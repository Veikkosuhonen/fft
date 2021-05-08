package com.github.veikkosuhonen.fftapp.fft.windowing;

/**
 * WindowFunction is used to calculate the window coefficient for each element in an array of known size. The coefficient
 * is always in the range (0, 1)
 */
public interface WindowFunction {
    /**
     * Calculates the coefficient of an element by index.
     * @param n The index for which coefficient is to be calculated
     * @return the coefficient in the range (0, 1)
     */
    public double getCoefficient(int n);
}
