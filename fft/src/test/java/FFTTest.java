import com.github.veikkosuhonen.fftapp.fft.*;
import org.junit.Assert;
import org.junit.Test;
import utils.Benchmark;
import com.github.veikkosuhonen.fftapp.fft.ReferenceFFT;
import utils.Signal;

import java.util.Arrays;

public class FFTTest {

    double[] periods = new double[]{1.0, 10.0, 100.0};

    @Test
    public void testNaiveDFTMatchesReference() {
        DFT dft = new NaiveDFT();
        DFT fft = new ReferenceFFT();

        double[][] signal = Signal.generateSine(64, 4.0, Math.PI);

        double[][] Fx = dft.process(signal);

        double[][] rFx = fft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], 0.001);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], 0.001);
    }

    @Test
    public void testNaiveDFTCorrectness() {
        DFT dft = new NaiveDFT();

        double[][] signal = Signal.generateSineComposite(64, new double[]{1, 3, 5});
        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 1", 0.5, fx[0][1], 0.001);
        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], 0.001);
        Assert.assertEquals("Calculates freq 5", 0.5, fx[0][5], 0.001);
    }

    @Test
    public void tesFFTMatchesReference() {
        DFT fft = new FFT();
        DFT rfft = new ReferenceFFT();

        double[][] signal = Signal.generateSineComposite(64, periods);

        double[][] Fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], 0.001);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], 0.001);
    }

    @Test
    public void testDFTPerformance() {
        int samples = 1024;
        DFT rfft = new ReferenceFFT();
        System.out.println(Benchmark.benchmarkDFT(rfft, samples, 10)/1e6 + " ms");
        DFT fft = new FFT();
        System.out.println(Benchmark.benchmarkDFT(fft, samples, 10)/1e6 + " ms");
        DFT dft = new NaiveDFT();
        System.out.println(Benchmark.benchmarkDFT(dft, samples, 10)/1e6 + " ms");
    }
}