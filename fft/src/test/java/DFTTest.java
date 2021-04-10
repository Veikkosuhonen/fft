import com.github.veikkosuhonen.fftapp.fft.dft.*;
import org.junit.Assert;
import org.junit.Test;
import utils.Signal;

public class DFTTest {

    double MAX_ERROR = 0.001;

    @Test
    public void testNaiveDFTMatchesReference() {
        DFT dft = new NaiveDFT();
        DFT fft = new ReferenceFFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSine(n, 4.0, Math.PI), new double[n]};

        double[][] Fx = dft.process(signal);
        double[][] rFx = fft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], 0.001);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], 0.001);
    }

    @Test
    public void testNaiveDFTCorrectness() {
        DFT dft = new NaiveDFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], MAX_ERROR);
        Assert.assertEquals("Calculates freq 10", 0.5, fx[0][10], MAX_ERROR);
        Assert.assertEquals("Calculates freq 28", 0.5, fx[0][28], MAX_ERROR);
    }

    @Test
    public void testFFTMatchesReference() {
        DFT fft = new FFT();
        DFT rfft = new ReferenceFFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] Fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], MAX_ERROR);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], MAX_ERROR);
    }

    @Test
    public void testFFTCorrectness() {
        DFT dft = new FFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], MAX_ERROR);
        Assert.assertEquals("Calculates freq 10", 0.5, fx[0][10], MAX_ERROR);
        Assert.assertEquals("Calculates freq 28", 0.5, fx[0][28], MAX_ERROR);
    }

    @Test
    public void testInPlaceFFTMatchesReference() {
        DFT fft = new InPlaceFFT();
        DFT rfft = new ReferenceFFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] Fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], MAX_ERROR);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], MAX_ERROR);
    }

    @Test
    public void testInPlaceFFTCorrectness() {
        DFT dft = new InPlaceFFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], MAX_ERROR);
        Assert.assertEquals("Calculates freq 10", 0.5, fx[0][10], MAX_ERROR);
        Assert.assertEquals("Calculates freq 28", 0.5, fx[0][28], MAX_ERROR);
    }
}