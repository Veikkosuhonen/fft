package com.github.veikkosuhonen.fftapp.fft;

public class MockFFT implements FFT {
    @Override
    public int[] process(byte[] data) {
        return new int[data.length];
    }
}
