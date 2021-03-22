import com.github.veikkosuhonen.fftapp.fft.*;
import org.junit.Assert;
import org.junit.Test;
import utils.ReferenceFFT;
import utils.Signal;

public class FFTTest {
    
    @Test
    public void testNaiveDFTMatchesReference() {
        DFT dft = new NaiveDFT();
        DFT fft = new ReferenceFFT();

        double[][] signal = Signal.generateSine(64, 4.0, Math.PI);

        double[][] Fx = dft.process(signal);

        double[][] rFx = fft.process(signal);

        Assert.assertArrayEquals("Real results close to reference", rFx[0], Fx[0], 0.001);
        Assert.assertArrayEquals("Imaginary results close to reference", rFx[1], Fx[1], 0.001);
        System.out.println(Fx[0][4]);
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
    public void testNaiveDFTPerformance() {

    }
}