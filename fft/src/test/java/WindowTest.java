import com.github.veikkosuhonen.fftapp.fft.windowing.Hann;
import com.github.veikkosuhonen.fftapp.fft.windowing.Square;
import com.github.veikkosuhonen.fftapp.fft.windowing.WindowFunction;
import org.junit.Assert;
import org.junit.Test;

public class WindowTest {

    final double DELTA = 1e-4;

    @Test
    public void testSquare() {
        WindowFunction square = new Square();
        Assert.assertEquals(1.0, square.getCoefficient(11), DELTA);
    }

    @Test
    public void testHann() {
        int N = 1024;
        WindowFunction hann = new Hann(N);
        Assert.assertEquals("n = 0 returns 0", 0.0, hann.getCoefficient(0), DELTA);
        Assert.assertEquals("Is symmetric", hann.getCoefficient(N / 4), hann.getCoefficient(N / 2 + N / 4), DELTA);
        Assert.assertEquals("n = N / 2 returns 1", 1.0, hann.getCoefficient(N / 2), DELTA);
        Assert.assertEquals("n = N returns 0", 0.0, hann.getCoefficient(N), DELTA);
    }
}
