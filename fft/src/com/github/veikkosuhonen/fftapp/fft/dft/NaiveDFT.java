package com.github.veikkosuhonen.fftapp.fft.dft;

import com.github.veikkosuhonen.fftapp.fft.utils.Complex;

/**
 * A naive implementation of the {@link DFT} with O(n^2) runtime
 */
public class NaiveDFT implements DFT {

    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
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

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }
}
