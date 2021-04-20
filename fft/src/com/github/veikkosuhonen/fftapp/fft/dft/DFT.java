package com.github.veikkosuhonen.fftapp.fft.dft;

/**
 * The DFT calculates the Discrete Fourier Transform of a complex signal.
 * <a href=https://en.wikipedia.org/wiki/Discrete_Fourier_transform>Wikipedia</a>
 */
public abstract class DFT {

    /**
     * @param dataRI Signal for which to calculate the DFT.
     *               Must consist of two arrays of equal length, the first representing real values
     *               and the second representing imaginary values. The length of the arrays must be a power of two.
     * @return The DFT of the signal. Consists of two equal length arrays, the first representing real values
     * and the second representing imaginary values.
     */
    public abstract double[][] process(double[][] dataRI) throws IllegalArgumentException;


    /**
     * @param dataRI Signal for which to calculate the DFT.
     *               Must consist of two arrays of equal length, the first representing real values
     *               and the second representing imaginary values. The length of the arrays must be a power of two.
     * @param normalize Whether to normalize the data
     * @return The DFT of the signal. Array of two double arrays, the first representing real values
     * and the second representing imaginary values.
     */
    public abstract double[][] process(double[][] dataRI, boolean normalize) throws IllegalArgumentException;

    void validateInput(double[][] a) {
        int n = a[0].length;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Length of the real and imaginary signals must be a power of two (was " + n + ")");
        } else if (n != a[1].length) {
            throw new IllegalArgumentException("Length of the real and imaginary signal must match (" + n + " != " + a[1].length + ")");
        } else if (a.length != 2) {
            throw new IllegalArgumentException("Input signal should contain two arrays (got " + a.length + ")");
        }
    }
}