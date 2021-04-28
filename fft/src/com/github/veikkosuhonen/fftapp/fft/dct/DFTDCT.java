package com.github.veikkosuhonen.fftapp.fft.dct;

import com.github.veikkosuhonen.fftapp.fft.dft.DFT;

/**
 * Computes the Discrete Cosine Transform <a href="https://en.wikipedia.org/wiki/Discrete_cosine_transform#DCT-II">
 * using a DFT-algorithm.
 */
public class DFTDCT extends DCT {

    private DFT dft;

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
        double[] real = new double[n];
        for (int i = 0; i < n / 2; i++) {
            real[i] = signal[i * 2];
            real[n - 1 - i] = signal[i * 2 + 1];
        }

        double[][] signalRI = new double[][] {real, new double[n]};
        double[][] resultRI = dft.process(signalRI);

        real = resultRI[0];
        double[] img = resultRI[1];
        for (int i = 0; i < n; i++) {
            double temp = i * Math.PI / (2 * n);
            real[i] = real[i] * Math.cos(temp) + img[i] * Math.sin(temp);
        }
        return real;
    }
}