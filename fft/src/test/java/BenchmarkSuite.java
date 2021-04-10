import com.github.veikkosuhonen.fftapp.fft.dft.DFT;
import com.github.veikkosuhonen.fftapp.fft.dft.FFT;
import com.github.veikkosuhonen.fftapp.fft.dft.InPlaceFFT;
import com.github.veikkosuhonen.fftapp.fft.dft.ReferenceFFT;
import com.github.veikkosuhonen.fftapp.fft.utils.ArrayUtils;
import utils.ChartBuilder;
import utils.PlotBuilder;
import utils.Signal;

/**
 * Contains a main method to run a suite of benchmarks and plot the results
 */
public class BenchmarkSuite {
    public static void main(String[] args) {

        int trials = 100;
        int warmup = 50;
        int sizes = 10;
        double[] fftTime = new double[sizes];
        double[] inPlaceFFTime = new double[sizes];
        double[] referenceFFTTime = new double[sizes];
        int[] sampleSizes = new int[sizes];

        for (int i = 0; i < sizes; i++) {
            int samples = 2 << i + 6;
            fftTime[i] = benchmark(new FFT(), samples, trials, warmup);
            inPlaceFFTime[i] = benchmark(new InPlaceFFT(), samples, trials, warmup);
            referenceFFTTime[i] = benchmark(new ReferenceFFT(), samples, trials, warmup);
            sampleSizes[i] = samples;
        }

        new PlotBuilder()
                .addChart(new ChartBuilder()
                        .addSeries(fftTime, sampleSizes, "FFT")
                        .addSeries(inPlaceFFTime, sampleSizes, "In-place FFT")
                        .addSeries(referenceFFTTime, sampleSizes, "Reference FFT")
                        .build("Average run times", "n samples", "runtime (ms)"))
                .show();
    }

    public static double benchmark(DFT dft, int samples, int trials, int warmup) {
        double[][] signal = new double[][] {Signal.generateSineComposite(samples, new double[]{1.0, 2.89, 7.1}, new double[]{0.1, 0.5, -10.0}), new double[samples]};
        double avgTime = 0;
        for (int i = 0; i < trials + warmup; i++) {
            long start = System.nanoTime();
            dft.process(signal);
            long end = System.nanoTime();
            if (i >= warmup) {
                avgTime += ((end - start) / 1e6);
            }
        }
        return 1.0 * avgTime / trials;
    }
}
