import com.github.veikkosuhonen.fftapp.fft.utils.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ArrayUtilsTest {

    @Test
    public void testSlice() {
        float[] arr = new float[] {1, 2, 3, 4, 5, 6};
        float[] arrSliced1 = new float[] {1, 2, 3};
        float[] arrSliced2 = new float[] {3, 4, 5};

        Assert.assertArrayEquals(arrSliced1, ArrayUtils.slice(arr, 3), 0.001f);
        Assert.assertArrayEquals(arrSliced2, ArrayUtils.slice(arr, 2, 5), 0.001f);

        boolean exceptionThrown = false;
        try {
            ArrayUtils.slice(arr, 5, 5);
        } catch (IllegalArgumentException iae) {
            exceptionThrown = true;
        }
        Assert.assertTrue("Exception thrown when indices equal", exceptionThrown);
    }

    @Test
    public void testJoin() {
        float[] arr = new float[] {1, 2, 3, 4, 5, 6};
        float[] arr1 = new float[] {1, 2};
        float[] arr2 = new float[] {3, 4, 5, 6};
        Assert.assertArrayEquals(arr, ArrayUtils.join(arr1, arr2), 0.001f);
    }

    @Test
    public void testAbs() {
        float[] arr1 = new float[] {1, 2, 3, 4, 5, 6};
        float[] arr2 = new float[] {1, -2, 3, -4, 5, -6};
        Assert.assertArrayEquals(arr1, ArrayUtils.abs(arr2), 0.001f);
    }


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

    @Test
    public void testScale() {
        float[] arr = new float[] {5, 3, 2, 4, 1};
        ArrayUtils.scale(arr, -1, 1);
        System.out.println(Arrays.toString(arr));
        float[] expected = new float[] {1f, 0f, -0.5f, 0.5f, -1f};
        Assert.assertArrayEquals(expected, arr, 0.001f);
    }

    @Test
    public void testClamp() {
        float[] arr = new float[] {5, 3, 2, 4, 1};
        ArrayUtils.clamp(arr, 2, 3);
        float[] expected = new float[] {3, 3, 2, 3, 2};
        Assert.assertArrayEquals(expected, arr, 0.001f);
    }
}
