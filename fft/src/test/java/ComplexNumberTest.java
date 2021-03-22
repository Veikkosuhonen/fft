import com.github.veikkosuhonen.fftapp.fft.Complex;
import org.junit.Assert;
import org.junit.Test;

public class ComplexNumberTest {

    @Test
    public void testAdd() {
        Complex a = new Complex(2, 4);
        Complex b = new Complex(-3, 1);
        Complex c = new Complex(-1, 5);
        a.add(b);
        Assert.assertEquals(c, a);
    }

    @Test
    public void testMult() {
        Complex a = new Complex(3, 2);
        Complex b = new Complex(1, 4);
        Complex c = new Complex(-5, 14);
        a.mult(b);
        Assert.assertEquals(c, a);
    }
}
