package com.github.veikkosuhonen.fftapp.fft;

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
        // Make a copy of the original array, we don't want to modify it
        double[][] dataRICopy = new double[2][dataRI[0].length];
        System.arraycopy(dataRI[0], 0, dataRICopy[0], 0, dataRI[0].length);
        System.arraycopy(dataRI[1], 0, dataRICopy[1], 0, dataRI[1].length);
        FastFourierTransformer.transformInPlace(dataRICopy, DftNormalization.STANDARD, TransformType.FORWARD);
        if (normalize) {
            for (int i = 0; i < dataRICopy[0].length; i++) {
                dataRICopy[0][i] /= dataRICopy[0].length;
                dataRICopy[1][i] /= dataRICopy[0].length;
            }
        }
        return dataRICopy;
    }
}
