package com.github.veikkosuhonen.fftapp.fft.dft;

import com.github.veikkosuhonen.fftapp.fft.utils.Complex;

public class InPlaceFFT implements DFT {

    private int[] bitReversalPermutation;

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        int n = dataRI[0].length;

        calculateBitReversalPermutation(n);

        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) {
            data[i] = new Complex(dataRI[0][i], dataRI[1][i]);
        }
        processInPlace(data);

        double[][] dataRICopy = new double[2][n];
        if (normalize) {
            for (int i = 0; i < dataRI[0].length; i++) {
                dataRICopy[0][i] = data[i].real / n;
                dataRICopy[1][i] = data[i].img / n;
            }
        } else {
            for (int i = 0; i < n; i++) {
                dataRICopy[0][i] = data[i].real;
                dataRICopy[1][i] = data[i].img;
            }
        }
        return dataRICopy;
    }

    private void processInPlace(Complex[] a) {
        int n = a.length;

        // Reorder the input array by applying bit-reversal permutation
        for (int i = 0; i < n; i++) {
            int k = bitReversalPermutation[i];
            if (i < k) {
                Complex temp = a[i];
                a[i] = a[k];
                a[k] = temp;
            }
        }

        for (int len = 2; len <= n; len <<= 1) {
            double angle = 2 * Math.PI / len;
            Complex wlen = new Complex(Math.cos(angle), Math.sin(angle));
            for (int i = 0; i < n; i += len) {
                Complex w = new Complex(1, 0);
                for (int j = 0; j < len / 2; j++) {
                    Complex u = a[i + j];
                    Complex v = a[i + j + len / 2].times(w);
                    a[i + j] = u.plus(v);
                    a[i + j + len / 2] = u.minus(v);
                    w = w.times(wlen);
                }
            }
        }
    }

    private void calculateBitReversalPermutation(int n) {
        if (bitReversalPermutation != null && bitReversalPermutation.length == n) {
            return;
        }
        bitReversalPermutation = new int[n];
        int lg_n = log2ceil(n);
        for (int i = 0; i < n; i++) {
            int k = reverse(i, lg_n);
            bitReversalPermutation[i] = k;
        }
    }

    /**
     * Reverses the last <code>N</code> bits of an integer.
     * <p>
     * Example: <code>a = 8 = 0b1... 00001000</code> and <code>n = 5</code>.
     * The 5 least significant bits will be reversed and the result will be
     * <code>0b1... 00000010 = 2</code>.
     * </p>
     *
     * @param a The integer whose bits are to be reversed
     * @param n The number of bits to reverse
     * @return The reversed value
     */
    private int reverse(int a, int n) {
        int res = 0;
        for (int i = 0; i < n; i++) {
            if ((a & (1 << i)) != 0) {
                res |= 1 << (n - 1 - i);
            }
        }
        return res;
    }

    /**
     * Calculates the ceiled integer value of <code>log2(n)</code>,
     * or the minimum number of bits needed to represent an integer with magnitude <code>n</code>.
     *
     * @param n The integer value
     * @return The integer result of <code>ceil(log2(n))</code>
     */
    private int log2ceil(int n) {
        int log2 = 0;
        while ((1 << log2) < n) {
            log2++;
        }
        return log2;
    }
}
