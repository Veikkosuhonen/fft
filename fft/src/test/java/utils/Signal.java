package utils;

/**
 * Class for generating signals
 */
public class Signal {

    public static double[][] generateSine(int N, double K, double phase) {
        double[] real = new double[N];
        for (int i = 0; i < N; i++) {
            real[i] = Math.cos(2 * Math.PI / N * K * i + phase);
        }
        return new double[][]{real, new double[N]};
    }

    public static double[][] generateSine(int N, double K) {
        return generateSine(N, K, 0.0);
    }

    public static double[][] generateSineComposite(int N, double[] K, double[] phase) {
        if (K.length != phase.length) {
            throw new IllegalArgumentException("K and phase arrays must be the same length");
        }
        double[] real = new double[N];
        for (int j = 0; j < K.length; j++) {
            for (int i = 0; i < N; i++) {
                real[i] += Math.cos(2 * Math.PI / N * K[j] * i + phase[j]);
            }
        }
        return new double[][]{real, new double[N]};
    }

    public static double[][] generateSineComposite(int N, double[] K) {
        return generateSineComposite(N, K, new double[K.length]);
    }

}
