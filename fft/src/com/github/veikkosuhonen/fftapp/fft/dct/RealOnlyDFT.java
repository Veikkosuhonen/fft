package com.github.veikkosuhonen.fftapp.fft.dct;

import com.github.veikkosuhonen.fftapp.fft.dft.DFT;

/**
 * Not actual DCT, instead used for comparing DCTs to a DFT of same input length.
 */
public class RealOnlyDFT extends DCT {

    private DFT dft;

    /**
     * @param dft The DFT object to be used for calculation
     */
    public RealOnlyDFT(DFT dft) {
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

        // create an input array for the dft with zero imaginary components.
        double[][] signalRI = new double[][] {signal, new double[n]};
        double[][] resultRI = dft.process(signalRI);

        return resultRI[0];
    }
}
