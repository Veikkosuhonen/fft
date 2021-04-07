import com.github.veikkosuhonen.fftapp.fft.*;
import utils.Signal;

/**
 * Contains a main method to run a suite of benchmarks and plot the results
 */
public class BenchmarkSuite {
    public static void main(String[] args) {
        int samples = 2 << 8;
        int trials = 200;
        int warmup = 100;
        //long naiveDFTTime = benchmark(new NaiveDFT(), samples, trials, warmup);
        long FFTTime = benchmark(new FFT(), samples, trials, warmup);
        long inPlaceFFTime = benchmark(new InPlaceFFT(), samples, trials, warmup);
        long referenceFFTTime = benchmark(new ReferenceFFT(), samples, trials, warmup);
        //System.out.println("naive dft: " + naiveDFTTime / 1e6 + " ms");
        System.out.println("fft: " + FFTTime / 1e6 + " ms");
        System.out.println("in-place fft: " + inPlaceFFTime / 1e6 + " ms");
        System.out.println("reference fft: " + referenceFFTTime / 1e6 + " ms");
    }

    public static long benchmark(DFT dft, int samples, int trials, int warmup) {
        double[][] signal = Signal.generateSineComposite(samples, new double[]{1.0, 2.89, 7.1}, new double[]{0.1, 0.5, -10.0});
        long avgTime = 0;
        for (int i = 0; i < trials + warmup; i++) {
            long start = System.nanoTime();
            dft.process(signal);
            long end = System.nanoTime();
            if (i >= warmup) {
                avgTime += end - start;
            }
        }
        return avgTime / trials;
    }
}
