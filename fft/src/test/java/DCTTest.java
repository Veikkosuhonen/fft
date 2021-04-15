import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.dct.FastDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.RealOnlyDFT;
import com.github.veikkosuhonen.fftapp.fft.dft.DFT;
import com.github.veikkosuhonen.fftapp.fft.dft.ReferenceFFT;
import org.junit.Assert;
import org.junit.Test;
import utils.Signal;

public class DCTTest {

    double MAX_ERROR = 0.001;

    @Test
    public void testRealOnlyDCTMatchesDFT() {
        DFT dft = new ReferenceFFT();
        DCT dct = new RealOnlyDFT(new ReferenceFFT());

        int n = 128;
        double[] signal = Signal.generateSineComposite(n, new double[]{3, 10, 28});

        double[] fx = dct.process(signal);
        double[] rFx = dft.process(new double[][] {signal, new double[n]})[0];

        Assert.assertArrayEquals(rFx, fx, MAX_ERROR);
    }

    @Test
    public void testFastDCT() {
        DCT dct = new FastDCT();
        DCT reference = new RealOnlyDFT(new ReferenceFFT());

        int n = 128;
        double[] signal = Signal.generateSineComposite(n, new double[]{3, 10, 28});

        double[] fx = dct.process(signal);
        double[] rFx = reference.process(signal);

        Assert.assertArrayEquals(rFx, fx, MAX_ERROR);
    }
}
