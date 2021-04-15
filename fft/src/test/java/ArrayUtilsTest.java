import com.github.veikkosuhonen.fftapp.fft.utils.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

public class ArrayUtilsTest {

    @Test
    public void testSelect() {
        float[] arr = new float[] {5, 3, 2, 4, 1};
        Assert.assertEquals(5, ArrayUtils.select(arr, 0, arr.length - 1, arr.length - 1), 0);
        Assert.assertEquals(3, ArrayUtils.select(arr, 0, arr.length - 1, arr.length / 2), 0);
        Assert.assertEquals(1, ArrayUtils.select(arr, 0, arr.length - 1, 0), 0);

        arr = new float[] {1};
        Assert.assertEquals(1, ArrayUtils.select(arr, 0, arr.length - 1, arr.length / 2), 0);

        arr = new float[] {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0};
        Assert.assertEquals(1, ArrayUtils.select(arr, 0, arr.length - 1, arr.length - 1), 0);
        Assert.assertEquals(0, ArrayUtils.select(arr, 0, arr.length - 1, arr.length / 2), 0);
        Assert.assertEquals(0, ArrayUtils.select(arr, 0, arr.length - 1, 0), 0);
    }
}
