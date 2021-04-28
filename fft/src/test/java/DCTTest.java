import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.dct.FastDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.NaiveDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.RealOnlyDFT;
import com.github.veikkosuhonen.fftapp.fft.dft.DFT;
import com.github.veikkosuhonen.fftapp.fft.dft.ReferenceFFT;
import org.junit.Assert;
import org.junit.Test;
import utils.Signal;

import java.util.Arrays;

public class DCTTest {

    double MAX_ERROR = 0.001;

    /*@Test
    public void testRealOnlyDCTMatchesDFT() {
        DFT dft = new ReferenceFFT();
        DCT dct = new RealOnlyDFT(new ReferenceFFT());

        int n = 128;
        double[] signal = Signal.generateSineComposite(n, new double[]{3, 10, 28});

        double[] fx = dct.process(signal);
        double[] rFx = dft.process(new double[][] {signal, new double[n]})[0];

        Assert.assertArrayEquals(rFx, fx, MAX_ERROR);
    }*/

    @Test
    public void testFastDCT() {
        DCT naiveDct = new NaiveDCT();
        DCT fdct = new FastDCT();
        DCT reference = new RealOnlyDFT(new ReferenceFFT());

        int n = 8;
        double[] signal = Signal.generateSineComposite(n, new double[]{3, 10, 28});

        double[] nFx = naiveDct.process(signal);
        double[] fx = fdct.process(signal);
        double[] rFx = reference.process(signal);
        System.out.println(Arrays.toString(nFx));
        System.out.println(Arrays.toString(fx));
        System.out.println(Arrays.toString(rFx));
        Assert.assertArrayEquals(rFx, fx, MAX_ERROR);
    }
}
