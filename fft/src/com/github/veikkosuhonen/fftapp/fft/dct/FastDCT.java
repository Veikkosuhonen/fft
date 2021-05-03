package com.github.veikkosuhonen.fftapp.fft.dct;

public class FastDCT extends DCT {

    @Override
    public double[] process(double[] signal) {
        int n = signal.length;
        super.validateInput(n);
        double[] signalCopy = new double[n];
        System.arraycopy(signal, 0, signalCopy,0, n);
        processInPlace(signalCopy, 0, n, new double[n]);
        for (int i = 0; i < n; i++) {
            signalCopy[i] /= n;
        }
        return signalCopy;
    }

    private void processInPlace(double[] data, int offset, int len, double[] temp) {
        if (len == 1) return;

        int halfLen = len / 2;

        for (int i = 0; i < halfLen; i++) {
            double x = data[offset + i];
            double y = data[offset + len - 1 - i];
            temp[offset + i] = x + y;
            temp[offset + i + halfLen] = (x - y) / ( Math.cos((i + 0.5) * Math.PI / len) * 2);
        }

        processInPlace(temp, offset, halfLen, data);
        processInPlace(temp, offset + halfLen, halfLen, data);

        for (int i = 0; i < halfLen - 1; i++) {
            data[offset + i * 2] = temp[offset + i];
            data[offset + i * 2 + 1] = temp[offset + i + halfLen] + temp[offset + i + halfLen + 1];
        }

        data[offset + len - 2] = temp[offset + halfLen - 1];
        data[offset + len - 1] = temp[offset + len - 1];
    }
}
