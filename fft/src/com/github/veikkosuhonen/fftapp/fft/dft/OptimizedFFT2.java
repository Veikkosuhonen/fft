package com.github.veikkosuhonen.fftapp.fft.dft;

/**
 * Adds even more tricks from various sources:
 * trigonometric tables: https://www.nayuki.io/page/free-small-fft-in-multiple-languages
 * 4-term DFT: https://github.com/apache/commons-math/blob/master/src/main/java/org/apache/commons/math4/transform/FastFourierTransformer.java
 */
public class OptimizedFFT2 extends DFT {

    private int[] bitReversalPermutation;
    private double[] cosineTable;
    private double[] sineTable;

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    /**
     * Uses the same algorithm as InPlaceFFT but without Complex objects, avoiding {@code O(n)} object creation operations
     * @param dataRI the input signal
     * @param normalize whether to normalize (divide by {@code n}) the data
     * @return the calculated DFT
     */
    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        super.validateInput(dataRI);
        int n = dataRI[0].length;
        computeBitReversalPermutation(n);
        computeTrigonometricTables(n);
        // Make a copy of the original array, we don't want to modify it
        double[][] dataRICopy = new double[2][n];
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

    /**
     * Computes the DFT in-place and stores the result in the input array
     * @param dataRI input data with real and imaginary arrays
     */
    private void processInPlace(double[][] dataRI) {
        double[] real = dataRI[0];
        double[] img = dataRI[1];
        int n = real.length;

        double tempR, tempI;
        int halfSize, step, l, p;
        // Reorder the input array by applying bit-reversal permutation
        for (int i = 0; i < n; i++) {
            p = bitReversalPermutation[i];
            if (i < p) {
                // swap i:th and k:th positions
                tempR = real[i];
                tempI = img[i];
                real[i] = real[p];
                img[i] = img[p];
                real[p] = tempR;
                img[p] = tempI;
            }
        }

        for (int size = 2; size <= n; size *= 2) {
            halfSize = size / 2;
            step = n / size;
            for (int i = 0; i < n; i += size) {
                for (int j = i, k = 0; j < i + halfSize; j++, k += step) {

                    l = j + halfSize;
                    tempR = real[l] * cosineTable[k] + img[l] * sineTable[k];
                    tempI = -real[l] * sineTable[k] + img[l] * cosineTable[k];

                    real[l] = real[j] - tempR;
                    img[l] = img[j] - tempI;
                    real[j] += tempR;
                    img[j] += tempI;
                }
            }
        }
    }

    /**
     * Precompute trigonometric tables for input size {@code n}.
     * Is stored for subsequent calls if {@code n} remains the same.
     * @param n
     */
    private void computeTrigonometricTables(int n) {
        int m = n / 2;
        if (cosineTable != null && cosineTable.length == m) {
            return;
        }
        cosineTable = new double[m];
        sineTable = new double[m];
        for (int i = 0; i < m; i++) {
            cosineTable[i] = Math.cos(2 * Math.PI * i / n);
            sineTable[i] = Math.sin(2 * Math.PI * i / n);
        }
    }

    /**
     * Computes the <a href=https://en.wikipedia.org/wiki/Bit-reversal_permutation>bit reversal permutation</a> for
     * an array of size {@code n}. No-op if the right sized permutation has already been computed and stored.
     * @param n the size of the array
     */
    private void computeBitReversalPermutation(int n) {
        if (bitReversalPermutation != null && bitReversalPermutation.length == n) {
            return;
        }
        bitReversalPermutation = new int[n];
        int lg_n = log2ceil(n);
        int k;
        for (int i = 0; i < n; i++) {
            k = reverse(i, lg_n);
            bitReversalPermutation[i] = k;
        }
    }

    /**
     * Reverses the last {@code N} bits of an integer.
     * <p>
     * Example: {@code a = 8 = 0b1... 00001000} and {@code n = 5}.
     * The 5 least significant bits will be reversed and the result will be
     * {@code 0b1... 00000010 = 2}.
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
     * Calculates the ceiled integer value of {@code log2(n)},
     * or the minimum number of bits needed to represent an integer with magnitude {@code n}.
     *
     * @param n The integer value
     * @return The integer result of {@code ceil(log2(n))}
     */
    private int log2ceil(int n) {
        int log2 = 0;
        while ((1 << log2) < n) {
            log2++;
        }
        return log2;
    }
}
