import com.github.veikkosuhonen.fftapp.fft.FFT;
import com.github.veikkosuhonen.fftapp.fft.MockFFT;
import org.junit.Assert;
import org.junit.Test;

public class FFTTest {

    @Test
    public void testTest() {
        Assert.assertTrue("test framework online", 2 + 2 == 4);
    }

    @Test
    public void testMockFFT() {
        FFT fft = new MockFFT();
        byte[] data = new byte[128];
        Assert.assertEquals("process returns same length", data.length, fft.process(data).length);
    }
}