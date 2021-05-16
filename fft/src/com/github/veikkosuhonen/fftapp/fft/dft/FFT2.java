package com.github.veikkosuhonen.fftapp.fft.dft;

import com.github.veikkosuhonen.fftapp.fft.utils.Complex;

/**
 * Radix-2 recursive FFT, very similar to {@see FFT} but with no {@code Complex} objects, with precomputed trigonometric
 * table, and 2-term dft.
 */
public class FFT2 extends DFT {

    private double[] cosineTable;
    private double[] sineTable;

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
        computeTrigonometricTable(n);
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
        if (n == 1) {
            return;
        } /* else if (n == 2) {
            // TODO radix-2
            // X_0 = x_0 + x_1
            dataRI[0][0] = dataRI[0][0] + dataRI[0][1];
            dataRI[1][0] = dataRI[1][0] + dataRI[1][1];
            // X_1 = x_0 - x_1
            dataRI[0][1] = dataRI[0][0] - dataRI[0][1];
            dataRI[1][1] = dataRI[1][0] - dataRI[1][1];
            return;
        }*/

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
        double wR, wI, wnR, wnI, temp;
        wR = 1.0;
        wI = 0.0;
        //double angle = 2 * Math.PI / n;
        wnR = cosineTable[n];
        wnI = sineTable[n];
        double aR, aI, bR, bI, cR, cI;
        for (int i = 0; i < n / 2; i++) {
            // Complex a from first half
            aR = dataRI0[0][i];
            aI = dataRI0[1][i];
            // Complex b from second half
            bR = dataRI1[0][i];
            bI = dataRI1[1][i];
            // Complex c = w * b
            cR = wR * bR - wI * bI;
            cI = wR * bI + wI * bR;

            dataRI[0][i] = aR + cR;
            dataRI[1][i] = aI + cI;
            dataRI[0][i + n/2] = aR - cR;
            dataRI[1][i + n/2] = aI - cI;

            // w *= wn
            temp = wR * wnR - wI * wnI;
            wI = wR * wnI + wI * wnR;
            wR = temp;
        }
    }

    private void computeTrigonometricTable(int n) {
        int m = n + 1;
        if (cosineTable != null && cosineTable.length == m) {
            return;
        }
        cosineTable = new double[m];
        sineTable = new double[m];
        double angle;
        for (int i = 2; i <= n; i = i << 1) {
            angle = 2 * Math.PI / i;
            cosineTable[i] = Math.cos(angle);
            sineTable[i] = Math.sin(angle);
        }
    }
}

