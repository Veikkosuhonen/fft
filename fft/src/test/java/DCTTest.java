import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.dct.FastDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.NaiveDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.DFTDCT;
import com.github.veikkosuhonen.fftapp.fft.dft.ReferenceFFT;
import org.junit.Assert;
import org.junit.Test;
import utils.Signal;

import java.util.Arrays;

public class DCTTest {

    double MAX_ERROR = 0.001;

    double[] signal = Signal.generateSineComposite(8, new double[]{3, 10, 28});
    double[] expected = new double[] {0.0, 0.4, 0.0, 0.6, 0.4, 0.4, 0.2, 0.4}; // These values come from testing with different DCT algorithms.
    double[] shortSignal = Signal.generateSine(4, 1.0);
    double[] shortExpected = new double[] {0.0, 0.33, 0.35, -0.14};

    @Test
    public void testDFTDCTCorrectness() {
        DCT dct = new DFTDCT(new ReferenceFFT());

        double[] fx = dct.process(signal);
        Assert.assertArrayEquals(expected, fx, 0.05);

        fx = dct.process(shortSignal);
        Assert.assertArrayEquals(shortExpected, fx, 0.05);
    }

    @Test
    public void testNaiveDCTCorrectness() {
        DCT dct = new NaiveDCT();

        double[] fx = dct.process(signal);
        Assert.assertArrayEquals(expected, fx, 0.05);

        fx = dct.process(shortSignal);
        Assert.assertArrayEquals(shortExpected, fx, 0.05);
    }

    @Test
    public void testFastDCTCorrectness() {
        DCT dct = new FastDCT();

        double[] fx = dct.process(signal);
        Assert.assertArrayEquals(expected, fx, 0.05);

        fx = dct.process(shortSignal);
        Assert.assertArrayEquals(shortExpected, fx, 0.05);
    }

    @Test
    public void testMatch() {
        DCT fdct = new FastDCT();
        DCT reference = new DFTDCT(new ReferenceFFT());

        int n = 8;
        double[] signal = Signal.generateSineComposite(n, new double[]{3, 10, 28});

        double[] fx = fdct.process(signal);
        double[] rFx = reference.process(signal);
        Assert.assertArrayEquals(rFx, fx, MAX_ERROR);
    }

    @Test
    public void testValidateInput() {
        DCT dft = new DCT() {
            @Override
            public double[] process(double[] dataRI) throws IllegalArgumentException {
                super.validateInput(dataRI.length);
                return new double[1];
            }
        };
        double[] invalidInput1 = new double[1023];
        double[] validInput1 = new double[1024];

        boolean invalid = false;
        try {
            dft.process(invalidInput1);
        } catch (IllegalArgumentException iae) {
            invalid = true;
        }
        Assert.assertTrue("throws on invalid1", invalid);

        invalid = false;
        try {
            dft.process(validInput1);
        } catch (IllegalArgumentException iae) {
            invalid = true;
        }
        Assert.assertFalse("does not throw on valid1", invalid);
    }
}
