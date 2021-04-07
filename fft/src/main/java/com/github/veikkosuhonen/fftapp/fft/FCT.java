package com.github.veikkosuhonen.fftapp.fft;

/**
 * FCT or Fast Cosine Transform calculates the Fourier Transform on purely real data and ignores the imaginary parts,
 * thus reducing computation in theory by a factor of two. Internally it calls an {@link FFT} implementation
 * on processed data
 */
public class FCT implements DFT {

    DFT fft;

    public FCT() {
        fft = new FFT();
    }

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        // Make a copy of the original array, we don't want to modify it
        double[][] dataRICopy = new double[2][dataRI[0].length / 2];

        int n = dataRI[0].length;
        //copy even and odd parts into real and imaginary
        for (int i = 0; i < n; i += 2) {
            dataRICopy[0][i / 2] = dataRI[0][i];
            dataRICopy[1][i / 2] = dataRI[1][i + 1];
        }
        dataRICopy = fft.process(dataRICopy, normalize);
        double[] data = new double[n];
        return new double[][] { data, dataRI[1] };
    }


}