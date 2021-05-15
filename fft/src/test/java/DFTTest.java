import com.github.veikkosuhonen.fftapp.fft.dft.*;
import org.junit.Assert;
import org.junit.Test;
import utils.Signal;


public class DFTTest {

    double MAX_ERROR = 0.001;

    /**
     * Procedurally test each DFT algorithm. Only real outputs are checked.
     */
    @Test
    public void testAllDFTCorrectness() {
        /*
        * frequencies in the generated signal. They should be in growing order.
        * Note that these cannot be arbitrary: if two or more waves amplify each other at these frequencies,
        * the calculated result will be 0.5 * N_of_resonating_waves, instead of the expected 0.5.
        */
        double[] frequencies = new double[]{3, 10, 28, 222};
        DFT[] dfts = new DFT[] {
                new NaiveDFT(), new FFT(), new InPlaceFFT(), new OptimizedInPlaceFFT(), new ParallelFFT(), new OptimizedFFT2(), new ReferenceFFT()
        };
        for (DFT dft : dfts) {
            // test input sizes from 1 to 2048
            for (int i = 0; i < 11; i++) {
                int inputSize = 1 << i;
                double[][] signal = new double[][] { Signal.generateSineComposite(inputSize, frequencies), new double[inputSize] };
                double[][] result = new double[1][inputSize];
                try {
                    result = dft.process(signal);
                } catch (Exception e) {
                    e.printStackTrace();
                    Assert.fail("Exception when testing " + dft.getClass().getName() + " when N=" + inputSize);
                }
                // check if each frequency correctly calculated
                for (double f : frequencies) {
                    if ((int) f >= inputSize) break;
                    Assert.assertEquals("Calculates freq " + f, 0.5, result[0][(int)f], MAX_ERROR);
                }
            }
        }
    }

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
    public void testParallelFFTMatchesReference() {
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