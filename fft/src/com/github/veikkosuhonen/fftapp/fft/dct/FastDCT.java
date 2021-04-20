package com.github.veikkosuhonen.fftapp.fft.dct;

public class FastDCT extends DCT {

    @Override
    public double[] process(double[] signal) {
        int n = signal.length;
        validateInput(n);

        double[] signalCopy = new double[n];
        System.arraycopy(signal, 0, signalCopy,0, n);
        processInPlace(signalCopy, 0, n, new double[n]);
        for (int i = 0; i < n; i++) {
            signalCopy[i] /= n;
        }
        return signalCopy;
    }

    private void processInPlace(double[] data, int off, int len, double[] temp) {
        if (len == 1) return;

        int halfLen = len / 2;

        for (int i = 0; i < halfLen; i++) {
            double x = data[off + i];
            double y = data[off + len - 1 - i];
            temp[off + i] = x + y;
            temp[off + i + halfLen] = (x - y) / ( Math.cos((i + 0.5) * Math.PI / len) * 2);
        }

        processInPlace(temp, off, halfLen, data);
        processInPlace(temp, off + halfLen, halfLen, data);

        for (int i = 0; i < halfLen - 1; i++) {
            data[off + i * 2] = temp[off + i];
            data[off + i * 2 + 1] = temp[off + i + halfLen] + temp[off + i + halfLen + 1];
        }

        data[off + len - 2] = temp[off + halfLen - 1];
        data[off + len - 1] = temp[off + len - 1];
    }

    private void validateInput(int n) {
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Length of the real and imaginary signals must be a power of two (was " + n + ")");
        }
    }
}
