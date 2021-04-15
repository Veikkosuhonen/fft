package com.github.veikkosuhonen.fftapp.fft.dct;

import com.github.veikkosuhonen.fftapp.fft.dft.DFT;

/**
 * A discrete cosine transform (DCT) expresses a finite sequence of data points in terms of a sum of cosine functions oscillating at different frequencies
 * (<a href=https://en.wikipedia.org/wiki/Discrete_cosine_transform>Wikipedia</a>). It achieves the same result as a {@link DFT} but for a real-only signal.
 */
public interface DCT {
    double[] process(double[] signal);
}
