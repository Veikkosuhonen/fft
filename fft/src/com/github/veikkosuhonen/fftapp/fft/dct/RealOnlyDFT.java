package com.github.veikkosuhonen.fftapp.fft.dct;

import com.github.veikkosuhonen.fftapp.fft.dft.DFT;

/**
 * SimpleRealOnlyDFT calculates the DCT using a DFT-algorithm by adding a zero imaginary component to the real signal
 */
public class RealOnlyDFT extends DCT {

    private DFT dft;

    /**
     * @param dft The DFT object to be used for calculation
     */
    public RealOnlyDFT(DFT dft) {
        this.dft = dft;
    }

    @Override
    public double[] process(double[] signal) {
        int n = signal.length;
        super.validateInput(n);
        double[] real = new double[n];
        for (int i = 0; i < n / 2; i++) {
            real[i] = signal[i * 2];
            real[n - 1 - i] = signal[i * 2 + 1];
        }

        double[][] signalRI = new double[][] {signal, new double[n]};
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