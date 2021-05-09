package com.github.veikkosuhonen.fftapp.fft.dct;

import com.github.veikkosuhonen.fftapp.fft.dft.DFT;

/**
 * Computes the Discrete Cosine Transform <a href="https://en.wikipedia.org/wiki/Discrete_cosine_transform#DCT-II">
 * using a DFT-algorithm.
 */
public class DFTDCT extends DCT {

    private DFT dft;
    private double[] cosineTable;
    private double[] sineTable;

    /**
     * @param dft The DFT object to be used for calculation
     */
    public DFTDCT(DFT dft) {
        this.dft = dft;
    }

    /**
     * @param signal The signal for which to calculate the DCT. The length of the array must be a power of two,
     *               else an <code>IllegalArgumentException is thrown</code>
     * @return The calculated DCT values.
     */
    @Override
    public double[] process(double[] signal) {
        int n = signal.length;
        super.validateInput(n);
        computeTrigonometricTables(n);
        /* Reorder the input data into a new array.
        For the DCT algorithm to work correctly, the even positions are set to the first half of the array
        and uneven to the second half of the array in reverse order */
        double[] real = new double[n];
        for (int i = 0; i < n / 2; i++) {
            real[i] = signal[i * 2];
            real[n - 1 - i] = signal[i * 2 + 1];
        }

        // create an input array for the dft with zero imaginary components.
        double[][] signalRI = new double[][] {real, new double[n]};
        double[][] resultRI = dft.process(signalRI);

        // transform the dft results to dct.
        real = resultRI[0];
        double[] img = resultRI[1];
        for (int i = 0; i < n; i++) {
            real[i] = real[i] * cosineTable[i] + img[i] * sineTable[i];
        }
        return real;
    }

    private void computeTrigonometricTables(int n) {
        if (cosineTable != null && cosineTable.length == n) {
            return;
        }
        cosineTable = new double[n];
        sineTable = new double[n];
        for (int i = 0; i < n; i++) {
            double x = i * Math.PI / (2 * n);
            cosineTable[i] = Math.cos(x);
            sineTable[i] = Math.sin(x);
        }
    }
}