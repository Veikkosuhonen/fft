package com.github.veikkosuhonen.fftapp.fft.dft;

/**
 * The DFT calculates the Discrete Fourier Transform of a complex signal.
 * <a href=https://en.wikipedia.org/wiki/Discrete_Fourier_transform>Wikipedia</a>
 */
public interface DFT {

    /**
     * @param dataRI Signal for which to calculate the DFT.
     *               Must consist of two arrays of equal length, the first representing real values
     *               and the second representing imaginary values.
     * @return The DFT of the signal. Consists of two equal length arrays, the first representing real values
     * and the second representing imaginary values.
     */
    public double[][] process(double[][] dataRI);


    /**
     * @param dataRI Signal for which to calculate the DFT.
     *               Must consist of two arrays of equal length, the first representing real values
     *               and the second representing imaginary values.
     * @param normalize Whether to normalize the data
     * @return The DFT of the signal. Array of two double arrays, the first representing real values
     * and the second representing imaginary values.
     */
    public double[][] process(double[][] dataRI, boolean normalize);
}