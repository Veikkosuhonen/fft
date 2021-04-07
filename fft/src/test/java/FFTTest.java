import com.github.veikkosuhonen.fftapp.fft.*;
import org.junit.Assert;
import org.junit.Test;
import com.github.veikkosuhonen.fftapp.fft.ReferenceFFT;
import utils.Signal;

import java.util.Arrays;

public class FFTTest {

    double MAX_ERROR = 0.001;

    @Test
    public void testNaiveDFTMatchesReference() {
        DFT dft = new NaiveDFT();
        DFT fft = new ReferenceFFT();

        double[][] signal = Signal.generateSine(128, 4.0, Math.PI);

        double[][] Fx = dft.process(signal);
        double[][] rFx = fft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], 0.001);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], 0.001);
    }

    @Test
    public void testNaiveDFTCorrectness() {
        DFT dft = new NaiveDFT();

        double[][] signal = Signal.generateSineComposite(128, new double[]{3, 10, 28});
        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], MAX_ERROR);
        Assert.assertEquals("Calculates freq 10", 0.5, fx[0][10], MAX_ERROR);
        Assert.assertEquals("Calculates freq 28", 0.5, fx[0][28], MAX_ERROR);
    }

    @Test
    public void testFFTMatchesReference() {
        DFT fft = new FFT();
        DFT rfft = new ReferenceFFT();

        double[][] signal = Signal.generateSineComposite(128, new double[]{1.0, 10.0, 100.0});

        double[][] Fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], MAX_ERROR);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], MAX_ERROR);
    }

    @Test
    public void testFFTCorrectness() {
        DFT dft = new FFT();

        double[][] signal = Signal.generateSineComposite(128, new double[]{3, 10, 28});
        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], MAX_ERROR);
        Assert.assertEquals("Calculates freq 10", 0.5, fx[0][10], MAX_ERROR);
        Assert.assertEquals("Calculates freq 28", 0.5, fx[0][28], MAX_ERROR);
    }

    @Test
    public void testInPlaceFFTMatchesReference() {
        DFT fft = new InPlaceFFT();
        DFT rfft = new ReferenceFFT();

        double[][] signal = Signal.generateSineComposite(128, new double[]{1.0, 10.0, 100.0});

        double[][] Fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], MAX_ERROR);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], MAX_ERROR);
    }

    @Test
    public void testInPlaceFFTCorrectness() {
        DFT dft = new InPlaceFFT();

        double[][] signal = Signal.generateSineComposite(32, new double[]{3, 10, 28});
        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], MAX_ERROR);
        Assert.assertEquals("Calculates freq 10", 0.5, fx[0][10], MAX_ERROR);
        Assert.assertEquals("Calculates freq 28", 0.5, fx[0][28], MAX_ERROR);
    }
}