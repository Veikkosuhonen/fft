package com.github.veikkosuhonen.fftapp.fft.dct;

import com.github.veikkosuhonen.fftapp.fft.dft.DFT;

/**
 * A discrete cosine transform (DCT) expresses a finite sequence of data points in terms of a sum of cosine functions oscillating at different frequencies
 * (<a href=https://en.wikipedia.org/wiki/Discrete_cosine_transform>Wikipedia</a>). In practise it achieves a (subjectively) similar result to a {@link DFT} on a real-only signal.
 */
public abstract class DCT {
    /**
     * @param signal The signal for which to calculate the DCT. The length of the array must be a power of two,
     *               else an <code>IllegalArgumentException is thrown</code>
     * @return The calculated DCT values.
     */
    public abstract double[] process(double[] signal) throws IllegalArgumentException;

    protected void validateInput(int n) {
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Length of the signal must be a power of two (was " + n + ")");
        }
    }
}
