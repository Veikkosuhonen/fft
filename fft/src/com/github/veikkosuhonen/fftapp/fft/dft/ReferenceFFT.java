package com.github.veikkosuhonen.fftapp.fft.dft;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Wraps the FastFourierTransform from Apache Commons math library for comparison purposes. The implementation uses magic.
 * <a href=https://github.com/apache/commons-math/blob/master/src/main/java/org/apache/commons/math4/transform/FastFourierTransformer.java>source</a>
 * A note from author, first time I read the source was 13/05 and the other FFT implementations in this project do not
 * borrow anything from the reference implementation.
 */
public class ReferenceFFT extends DFT {

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        super.validateInput(dataRI);
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
}
