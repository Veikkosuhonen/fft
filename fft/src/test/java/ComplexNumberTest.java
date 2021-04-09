import com.github.veikkosuhonen.fftapp.fft.utils.Complex;
import org.junit.Assert;
import org.junit.Test;

public class ComplexNumberTest {

    @Test
    public void testPlus() {
        Complex a = new Complex(2, 4);
        Complex b = new Complex(-3, 1);
        Complex c = new Complex(-1, 5);
        Assert.assertEquals(c, a.plus(b));
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(2, 4);
        Complex b = new Complex(-3, 1);
        Complex c = new Complex(5, 3);
        Assert.assertEquals(c, a.minus(b));
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(3, 2);
        Complex b = new Complex(1, 4);
        Complex c = new Complex(-5, 14);
        Assert.assertEquals(c, a.times(b));
    }

    @Test
    public void testEquals() {
        Complex a = new Complex(-3, 2);
        Complex b = new Complex(-3, 2);
        Complex c = new Complex(3, -2);
        Assert.assertEquals(a, a);
        Assert.assertEquals(a, b);
        Assert.assertNotEquals(a, c);
        Assert.assertNotEquals(a, new Object());
    }
}
