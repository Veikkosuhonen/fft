package com.github.veikkosuhonen.fftapp.fft.dft;

import com.github.veikkosuhonen.fftapp.fft.utils.Complex;

public class InPlaceFFT extends DFT {

    private int[] bitReversalPermutation;

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        super.validateInput(dataRI);
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

    /**
     * Uses the same in-place algorithm but without Complex objects, avoiding O(n) object creation operations
     * @param dataRI the input signal
     * @param normalize whether to normalize (divide by n) the data
     * @return the calculated DFT
     */
    public double[][] processExperimental(double[][] dataRI, boolean normalize) {
        super.validateInput(dataRI);
        int n = dataRI[0].length;
        calculateBitReversalPermutation(n);
        // Make a copy of the original array, we don't want to modify it
        double[][] dataRICopy = new double[2][dataRI[0].length];
        System.arraycopy(dataRI[0], 0, dataRICopy[0], 0, n);
        System.arraycopy(dataRI[1], 0, dataRICopy[1], 0, n);
        processInPlace(dataRICopy);
        if (normalize) {
            for (int i = 0; i < dataRI[0].length; i++) {
                dataRICopy[0][i] /= n;
                dataRICopy[1][i] /= n;
            }
        }
        return dataRICopy;
    }

    private void processInPlace(double[][] dataRI) {
        double[] real = dataRI[0];
        double[] img = dataRI[1];
        int n = real.length;

        // Reorder the input array by applying bit-reversal permutation
        for (int i = 0; i < n; i++) {
            int k = bitReversalPermutation[i];
            if (i < k) {
                double tempR = real[i];
                double tempI = img[i];
                real[i] = real[k];
                img[i] = img[k];
                real[k] = tempR;
                img[k] = tempI;
            }
        }

        for (int len = 2; len <= n; len <<= 1) {
            double angle = 2 * Math.PI / len;
            // Complex wlen
            double wlenR = Math.cos(angle);
            double wlenI = Math.sin(angle);
            for (int i = 0; i < n; i += len) {
                // Complex w
                double wR = 1.0;
                double wI = 0.0;
                for (int j = 0; j < len / 2; j++) {
                    // Complex u
                    double uR = real[i + j];
                    double uI = img[i + j];
                    // Complex v
                    double vR = real[i + j + len / 2];
                    double vI = img[i + j + len / 2];
                    // v = v * w
                    double vR0 = vR * wR - vI * wI;
                    vI = vR * wI + vI * wR;
                    vR = vR0;
                    // arr[i + j] = u + v
                    real[i + j] = uR + vR;
                    img[i + j] = uI + vI;
                    // arr[i + j + len / 2] = u - v
                    real[i + j + len / 2] = uR - vR;
                    img[i + j + len / 2] = uI - vI;
                    // w = w * wlen
                    double wR0 = wR * wlenR - wI * wlenI;
                    wI = wR * wlenI - wI * wlenR;
                    wR = wR0;
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
