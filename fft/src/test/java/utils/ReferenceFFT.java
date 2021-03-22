package utils;

import com.github.veikkosuhonen.fftapp.fft.DFT;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class ReferenceFFT implements DFT {

    @Override
    public double[][] process(double[][] dataRI) {
        return process(dataRI, true);
    }

    @Override
    public double[][] process(double[][] dataRI, boolean normalize) {
        FastFourierTransformer.transformInPlace(dataRI, DftNormalization.STANDARD, TransformType.FORWARD);
        if (normalize) {
            for (double[] data : dataRI) {
                for (int i = 0; i < data.length; i++) {
                    data[i] /= data.length;
                }
            }
        }
        return dataRI;
    }
}
