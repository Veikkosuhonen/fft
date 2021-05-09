package com.github.veikkosuhonen.fftapp.fft.dct;

public class FastDCT extends DCT {

    /**
     * @param signal The signal for which to calculate the DCT. The length of the array must be a power of two,
     *               else an <code>IllegalArgumentException is thrown</code>
     * @return The calculated DCT values.
     */
    @Override
    public double[] process(double[] signal) {
        int n = signal.length;
        super.validateInput(n);

        // Copy data
        double[] signalCopy = new double[n];
        System.arraycopy(signal, 0, signalCopy,0, n);

        // Call the recursive transform method
        processInPlace(signalCopy, 0, n, new double[n]);

        // Normalize
        for (int i = 0; i < n; i++) {
            signalCopy[i] /= n;
        }
        return signalCopy;
    }

    private void processInPlace(double[] data, int offset, int len, double[] temp) {
        if (len == 8) {
            dct8(data, offset);
            return;
        } else if (len == 1) return;

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


    /**
     * From https://www.nayuki.io/res/fast-discrete-cosine-transform-algorithms/FastDct8.java. Slightly modified to fit.
     * Computes the scaled DCT type II on the specified length-8 array in place.
     * @param vector the full data vector of numbers to transform
     * @param o the offset of the position to transform
     * @throws NullPointerException if the array is {@code null}
     */
    public static void dct8(double[] vector, int o) {
        // Algorithm by Arai, Agui, Nakajima, 1988. For details, see:
        // https://web.stanford.edu/class/ee398a/handouts/lectures/07-TransformCoding.pdf#page=30
        final double v0, v1, v2, v3, v4, v5, v6, v7, v8, v9,
                v10, v11, v12, v13, v14, v15, v16, v17, v18, v19,
                v20, v21, v22, v23, v24, v25, v26, v27, v28;

        v0 = vector[0 + o] + vector[7 + o];
        v1 = vector[1 + o] + vector[6 + o];
        v2 = vector[2 + o] + vector[5 + o];
        v3 = vector[3 + o] + vector[4 + o];
        v4 = vector[3 + o] - vector[4 + o];
        v5 = vector[2 + o] - vector[5 + o];
        v6 = vector[1 + o] - vector[6 + o];
        v7 = vector[0 + o] - vector[7 + o];

        v8 = v0 + v3;
        v9 = v1 + v2;
        v10 = v1 - v2;
        v11 = v0 - v3;
        v12 = -v4 - v5;
        v13 = (v5 + v6) * A[3];
        v14 = v6 + v7;

        v15 = v8 + v9;
        v16 = v8 - v9;
        v17 = (v10 + v11) * A[1];
        v18 = (v12 + v14) * A[5];

        v19 = -v12 * A[2] - v18;
        v20 = v14 * A[4] - v18;

        v21 = v17 + v11;
        v22 = v11 - v17;
        v23 = v13 + v7;
        v24 = v7 - v13;

        v25 = v19 + v24;
        v26 = v23 + v20;
        v27 = v23 - v20;
        v28 = v24 - v19;

        vector[0 + o] = S[0] * v15 * 2; // multiplied by 2 to be consisted with normalization
        vector[1 + o] = S[1] * v26 * 2;
        vector[2 + o] = S[2] * v21 * 2;
        vector[3 + o] = S[3] * v28 * 2;
        vector[4 + o] = S[4] * v16 * 2;
        vector[5 + o] = S[5] * v25 * 2;
        vector[6 + o] = S[6] * v22 * 2;
        vector[7 + o] = S[7] * v27 * 2;
    }

    /*---- Tables of constants for dct8 ----*/
    private static double[] S = new double[8];
    private static double[] A = new double[6];

    static {
        double[] C = new double[8];
        for (int i = 0; i < C.length; i++) {
            C[i] = Math.cos(Math.PI / 16 * i);
            S[i] = 1 / (4 * C[i]);
        }
        S[0] = 1 / (2 * Math.sqrt(2));
        // A[0] is unused
        A[1] = C[4];
        A[2] = C[2] - C[6];
        A[3] = C[4];
        A[4] = C[6] + C[2];
        A[5] = C[6];
    }
}
