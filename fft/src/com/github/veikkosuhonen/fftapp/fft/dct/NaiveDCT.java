package com.github.veikkosuhonen.fftapp.fft.dct;

/**
 * Implements the DCT-II algorithm
 */
public class NaiveDCT extends DCT {

    /**
     * Calculates the DCT according to the <a href=https://en.wikipedia.org/wiki/Discrete_cosine_transform#DCT-II>formula</a> (Wikipedia) in O(n^2) time.
     * @param signal The signal for which to calculate the DCT. The length of the array must be a power of two,
     *               else an <code>IllegalArgumentException is thrown</code>
     * @return the DCT values
     */
    @Override
    public double[] process(double[] signal) {
        int n = signal.length;
        super.validateInput(n);
        double[] result = new double[n];
        double pi_over_n = Math.PI / n;
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                sum += signal[j] * Math.cos(pi_over_n * i * (j + 0.5));
            }
            result[i] = sum / n; //Scaled by n
        }
        return result;
    }
}
