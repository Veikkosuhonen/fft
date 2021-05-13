package com.github.veikkosuhonen.fftapp.fft.dft;

import com.github.veikkosuhonen.fftapp.fft.utils.Complex;

/**
 * A naive implementation of the {@link DFT} with {@code O(n^2)} runtime
 */
public class NaiveDFT extends DFT {

    /**
     * Computes the DFT using the naive algorithm.
     * @param dataRI    Signal for which to calculate the DFT.
     *                  Must consist of two arrays of equal length, the first representing real values
     *                  and the second representing imaginary values. The length of the arrays must be a power of two.
     * @param normalize Whether to normalize the data
     * @return a new array of two arrays with the real and imaginary results
     */
    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        super.validateInput(dataRI);
        int N = dataRI[0].length;

        double[] real = new double[N];
        double[] img = new double[N];

        Complex intSum;

        for (int k = 0; k < N; k++) {

            intSum = new Complex(0, 0);
            for (int n = 0; n < N; n++) {
                double realPart = Math.cos(((Math.PI * 2) / N) * k * n);
                double imgPart = Math.sin(((Math.PI * 2) / N) * k * n);
                Complex w = new Complex(realPart, -imgPart);
                w = w.times(new Complex(dataRI[0][n], dataRI[1][n]));
                intSum = intSum.plus(w);
            }
            real[k] = intSum.real;
            img[k] = intSum.img;

            if (normalize) {
                real[k] /= N;
                img[k] /= N;
            }
        }
        return new double[][] {real, img};
    }

    /**
     * Computes the DFT using the naive algorithm.
     * @param dataRI    Signal for which to calculate the DFT.
     *                  Must consist of two arrays of equal length, the first representing real values
     *                  and the second representing imaginary values. The length of the arrays must be a power of two.
     * @return a new array of two arrays with the real and imaginary results
     */
    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }
}
