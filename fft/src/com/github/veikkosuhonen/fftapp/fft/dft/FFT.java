package com.github.veikkosuhonen.fftapp.fft.dft;

import com.github.veikkosuhonen.fftapp.fft.utils.Complex;

/**
 * The FFT or Fast Fourier Transform is an efficient implementation of the {@link DFT}
 * with a {@code O(n * log(n)) runtime. This particular implementation uses the
 * <a href=https://en.wikipedia.org/wiki/Fast_Fourier_transform#Cooley%E2%80%93Tukey_algorithm>Cooley-Tukey algorithm</a>.
 * Based on code from <a href=https://cp-algorithms.com/algebra/fft.html>https://cp-algorithms.com/algebra/fft.html</a>
 */
public class FFT extends DFT {

    /**
     * Calculates the DFT using the recursive FFT algorithm
     * @param dataRI Signal for which to calculate the DFT.
     *               Must consist of two arrays of equal length, the first representing real values
     *               and the second representing imaginary values. The length of the arrays must be a power of two.
     * @return real and imaginary array containing frequencies and phases
     */
    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    /**
     * Calculates the DFT using the recursive FFT algorithm
     * @param dataRI Signal for which to calculate the DFT.
     *               Must consist of two arrays of equal length, the first representing real values
     *               and the second representing imaginary values. The length of the arrays must be a power of two.
     * @param normalize whether to normalize the data (default true)
     * @return real and imaginary array containing frequencies and phases
     */
    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        super.validateInput(dataRI);
        int n = dataRI[0].length;
        // Make a copy of the original array, we don't want to modify it
        double[][] dataRICopy = new double[2][dataRI[0].length];
        System.arraycopy(dataRI[0], 0, dataRICopy[0], 0, dataRI[0].length);
        System.arraycopy(dataRI[1], 0, dataRICopy[1], 0, dataRI[1].length);

        processInPlace(dataRICopy);

        if (normalize) {
            for (int i = 0; i < dataRI[0].length; i++) {
                dataRICopy[0][i] /= n;
                dataRICopy[1][i] /= n;
            }
        }
        return dataRICopy;
    }

    /**
     * Recursively computes the DFT and writes the result to the input array
     * @param dataRI real and imaginary input data
     */
    private void processInPlace(double[][] dataRI) {
        int n = dataRI[0].length;
        if (n == 1) return; // Base case

        // Divide-and-conquer: split even and odd members into two halves
        double[][] dataRI0 = new double[2][n / 2];
        double[][] dataRI1 = new double[2][n / 2];
        for (int i = 0; i < n; i += 2) {
            dataRI0[0][i / 2] = dataRI[0][i];
            dataRI0[1][i / 2] = dataRI[1][i];
            dataRI1[0][i / 2] = dataRI[0][i + 1];
            dataRI1[1][i / 2] = dataRI[1][i + 1];
        }

        processInPlace(dataRI0);
        processInPlace(dataRI1);

        // Merge
        double angle = 2 * Math.PI / n;
        Complex w = new Complex(1, 0);
        Complex wn = new Complex(Math.cos(angle), Math.sin(angle));

        for (int i = 0; i < n / 2; i++) {
            Complex a = new Complex(dataRI0[0][i], dataRI0[1][i]); // Complex from first half
            Complex b = new Complex(dataRI1[0][i], dataRI1[1][i]); // Complex from second half
            Complex p = a.plus(w.times(b)); // Resulting Complex in pos i
            Complex q = a.minus(w.times(b)); // Resulting Complex in pos i + n / 2
            dataRI[0][i] = p.real;
            dataRI[1][i] = p.img;
            dataRI[0][i + n/2] = q.real;
            dataRI[1][i + n/2] = q.img;
            w = w.times(wn);
        }
    }
}
