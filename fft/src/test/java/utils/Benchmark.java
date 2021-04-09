package utils;

import com.github.veikkosuhonen.fftapp.fft.dft.DFT;

/**
 * Provides methods to benchmark different DFT implementations
 */
public class Benchmark {

    /**
     * @param dft The DFT implementation to benchmark
     * @param samples Number of samples in the signal to be processed
     * @param runs How many runs should be done
     * @return The average time taken to calculate the dft of the signal
     */
    public static long benchmarkDFT(DFT dft, int samples, int runs) {
        long avgTime = 0L;
        boolean first = true;
        double[][] signal = Signal.generateSineComposite(samples, new double[]{1, 3, 5, 7});
        for (int i = 0; i < runs + 1; i++) {
            if (first) {
                first = false;
                dft.process(signal);
            } else {
                long start = System.nanoTime();
                dft.process(signal);
                avgTime += System.nanoTime() - start;
            }
        }
        return avgTime / runs;
    }
}
