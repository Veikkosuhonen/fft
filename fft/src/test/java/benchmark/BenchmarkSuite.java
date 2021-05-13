package benchmark;

import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.dct.DFTDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.FastDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.NaiveDCT;
import com.github.veikkosuhonen.fftapp.fft.dft.*;
import utils.ChartBuilder;
import utils.PlotBuilder;
import utils.Signal;

/**
 * Main method to run benchmarks and plot the results
 */
public class BenchmarkSuite {
    public static void main(String[] args) {
        System.out.println("############### Starting benchmark ##############");

        int trials = 100; // how many trials to run
        int warmup = 50; // how many warmup runs to do
        int sizes = 8; // how many (power of two) sizes of samples to do
        int startAt = 8; // smallest (power of two) sample size
        int[] sampleSizes = new int[sizes]; // For X-axis values

        //double[] naiveDFTTime = new double[sizes];
        double[] fftTime = new double[sizes];
        double[] inPlaceFFTime = new double[sizes];
        double[] parallelFFTTime = new double[sizes];
        double[] hybridParallelFFTTime = new double[sizes];
        double[] optimizedInPlaceFFTTime = new double[sizes];
        double[] referenceFFTTime = new double[sizes];

        System.out.println("===== Testing DFT algorithms (" + (2 << startAt) + " to " + (2 << startAt + sizes - 1) + " samples) ======");
        for (int i = 0; i < sizes; i++) {
            int samples = 2 << i + startAt;
            //naiveDFTTime[i] = samples > 1024 ? 0 : benchmarkDFT(new NaiveDFT(), samples, trials, warmup); // skip naive implementation on large samples
            fftTime[i] = benchmarkDFT(new FFT(), samples, trials, warmup);
            inPlaceFFTime[i] = benchmarkDFT(new InPlaceFFT(), samples, trials, warmup);
            parallelFFTTime[i] = benchmarkDFT(new ParallelFFT(), samples, trials, warmup);
            hybridParallelFFTTime[i] = benchmarkDFT(new HybridParallelFFT(), samples, trials, warmup);
            optimizedInPlaceFFTTime[i] = benchmarkDFT(new OptimizedInPlaceFFT(), samples, trials, warmup);
            referenceFFTTime[i] = benchmarkDFT(new ReferenceFFT(), samples, trials, warmup);
            sampleSizes[i] = i + startAt;

            System.out.println("Sample size  = " + samples);
            //System.out.println("Naive        = " + naiveDFTTime[i] + " ms");
            System.out.println("FFT          = " + fftTime[i] + " ms");
            System.out.println("InPlaceFFT   = " + inPlaceFFTime[i] + " ms");
            System.out.println("ParallelFFT  = " + parallelFFTTime[i] + " ms");
            System.out.println("Optimized    = " + optimizedInPlaceFFTTime[i] + " ms");
            System.out.println("ReferenceFFT = " + referenceFFTTime[i] + " ms");
            System.out.println();
        }

        //double[] naiveDCTTime = new double[sizes];
        double[] fastDCTTime = new double[sizes];
        double[] dftDctTime = new double[sizes];
        double[] referenceDftDctTime = new double[sizes];

        System.out.println();
        System.out.println("===== Testing DCT algorithms (" + (2 << startAt) + " to " + (2 << startAt + sizes - 1) + " samples) ======");
        for (int i = 0; i < sizes; i++) {
            int samples = 2 << i + startAt;
            //naiveDCTTime[i] = samples > 1024 ? 0 :benchmarkDCT(new NaiveDCT(), samples, trials, warmup); // skip naive implementation on large samples
            fastDCTTime[i] = benchmarkDCT(new FastDCT(), samples, trials, warmup);
            dftDctTime[i] = benchmarkDCT(new DFTDCT(new OptimizedInPlaceFFT()), samples, trials, warmup);
            referenceDftDctTime[i] = benchmarkDCT(new DFTDCT(new ReferenceFFT()), samples, trials, warmup);

            System.out.println("Sample size           = " + samples);
            //System.out.println("Naive                 = " + naiveDCTTime[i] + " ms");
            System.out.println("FastDCT               = " + fastDCTTime[i] + " ms");
            System.out.println("DFTDCT with FFT       = " + dftDctTime[i] + " ms");
            System.out.println("DFTDCT with reference = " + referenceDftDctTime[i] + " ms");
            System.out.println();
        }

        new PlotBuilder()
                .chart(new ChartBuilder()
                        //.series(naiveDFTTime, sampleSizes, "Naive DFT")
                        .series(fftTime, sampleSizes, "FFT")
                        .series(inPlaceFFTime, sampleSizes, "In-place FFT")
                        .series(parallelFFTTime, sampleSizes, "Parallel FFT")
                        .series(hybridParallelFFTTime, sampleSizes, "Hybrid Parallel FFT")
                        .series(optimizedInPlaceFFTTime, sampleSizes, "Optimized FFT")
                        .series(referenceFFTTime, sampleSizes, "Reference FFT")
                        .build("Average DFT run times", "n samples (log2)", "runtime (ms)"))
                .chart(new ChartBuilder()
                        //.series(naiveDCTTime, sampleSizes, "Naive DCT")
                        .series(fastDCTTime, sampleSizes, "FastDCT")
                        .series(dftDctTime, sampleSizes, "DCTDFT using FFT")
                        .series(referenceDftDctTime, sampleSizes, "DCTDFT using ReferenceFFT")
                        .build("Average DCT run times", "n samples (log2)", "runtime (ms)"))
                .show();
    }

    public static double benchmarkDFT(DFT dft, int samples, int trials, int warmup) {
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

    public static double benchmarkDCT(DCT dct, int samples, int trials, int warmup) {
        double[] signal = Signal.generateSineComposite(samples, new double[]{1.0, 2.89, 7.1}, new double[]{0.1, 0.5, -10.0});
        double avgTime = 0;
        for (int i = 0; i < trials + warmup; i++) {
            long start = System.nanoTime();
            dct.process(signal);
            long end = System.nanoTime();
            if (i >= warmup) {
                avgTime += ((end - start) / 1e6);
            }
        }
        return 1.0 * avgTime / trials;
    }
}
