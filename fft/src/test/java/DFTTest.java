import com.github.veikkosuhonen.fftapp.fft.dft.*;
import org.junit.Assert;
import org.junit.Test;
import utils.Signal;

import java.util.Arrays;

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

        double[][] fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], fx[0], MAX_ERROR);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], fx[1], MAX_ERROR);
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

    @Test
    public void testOptimizedInPlaceFFTMatchesReference() {
        DFT fft = new OptimizedInPlaceFFT();
        DFT rfft = new ReferenceFFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], fx[0], MAX_ERROR);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], fx[1], MAX_ERROR);
    }

    @Test
    public void testOptimizedInPlaceFFTCorrectness() {
        DFT dft = new OptimizedInPlaceFFT();

        int n = 32;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] fx = dft.process(signal);

        Assert.assertEquals("Calculates freq 3", 0.5, fx[0][3], MAX_ERROR);
        Assert.assertEquals("Calculates freq 10", 0.5, fx[0][10], MAX_ERROR);
        Assert.assertEquals("Calculates freq 28", 0.5, fx[0][28], MAX_ERROR);
    }

    @Test
    public void testParallelFFT() {
        DFT fft = new ParallelFFT();
        DFT rfft = new ReferenceFFT();

        int n = 128;
        double[][] signal = new double[][] {Signal.generateSineComposite(n, new double[]{3, 10, 28}), new double[n]};

        double[][] fx = fft.process(signal);
        double[][] rFx = rfft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], fx[0], MAX_ERROR);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], fx[1], MAX_ERROR);
    }

    @Test
    public void testValidateInput() {
        DFT dft = new DFT() {
            @Override
            public double[][] process(double[][] dataRI) throws IllegalArgumentException {
                return process(dataRI, true);
            }

            @Override
            public double[][] process(double[][] dataRI, boolean normalize) throws IllegalArgumentException {
                super.validateInput(dataRI);
                return new double[0][];
            }
        };
        double[][] invalidInput1 = new double[1][4];
        double[][] invalidInput2 = new double[2][3];
        double[][] invalidInput3 = new double[][] {new double[2], new double[4]};
        double[][] validInput1 = new double[2][1024];

        boolean invalid = false;
        try {
            dft.process(invalidInput1);
        } catch (IllegalArgumentException iae) {
            invalid = true;
        }
        Assert.assertTrue("throws on invalid1", invalid);

        invalid = false;
        try {
            dft.process(invalidInput2);
        } catch (IllegalArgumentException iae) {
            invalid = true;
        }
        Assert.assertTrue("throws on invalid2", invalid);

        invalid = false;
        try {
            dft.process(invalidInput3);
        } catch (IllegalArgumentException iae) {
            invalid = true;
        }
        Assert.assertTrue("throws on invalid3", invalid);

        invalid = false;
        try {
            dft.process(validInput1);
        } catch (IllegalArgumentException iae) {
            invalid = true;
        }
        Assert.assertFalse("does not throw on valid1", invalid);
    }
}