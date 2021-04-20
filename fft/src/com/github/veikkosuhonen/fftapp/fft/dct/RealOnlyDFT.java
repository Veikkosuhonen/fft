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
        double[][] signalRI = new double[][] {signal, new double[n]};
        double[][] resultRI = dft.process(signalRI);
        return resultRI[0];
    }
}