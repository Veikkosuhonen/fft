package com.github.veikkosuhonen.fftapp.fft.dft;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Wraps the FastFourierTransform from Apache Commons math library for comparison purposes
 */
public class ReferenceFFT implements DFT {

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        validateInput(dataRI);
        int n = dataRI[0].length;

        // Make a copy of the original array, we don't want to modify it
        double[][] dataRICopy = new double[2][n];
        System.arraycopy(dataRI[0], 0, dataRICopy[0], 0, n);
        System.arraycopy(dataRI[1], 0, dataRICopy[1], 0, n);
        FastFourierTransformer.transformInPlace(dataRICopy, DftNormalization.STANDARD, TransformType.FORWARD);
        if (normalize) {
            for (int i = 0; i < n; i++) {
                dataRICopy[0][i] /= n;
                dataRICopy[1][i] /= n;
            }
        }
        return dataRICopy;
    }

    private void validateInput(double[][] a) {
        int n = a[0].length;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Length of the real and imaginary signals must be a power of two (was " + n + ")");
        } else if (n != a[1].length) {
            throw new IllegalArgumentException("Length of the real and imaginary signal must match (" + n + " != " + a[1].length + ")");
        } else if (a.length != 2) {
            throw new IllegalArgumentException("Input signal should contain two arrays (got " + a.length + ")");
        }
    }
}
