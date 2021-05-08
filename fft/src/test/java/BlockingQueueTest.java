import com.github.veikkosuhonen.fftapp.fft.utils.BlockingQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class BlockingQueueTest {

    BlockingQueue queue;

    @Before
    public void init() {
        queue = new BlockingQueue(4, 2);
    }

    //@Test
    public void test() {
        queue.offer(new double[]{1, 1});
        queue.offer(new double[]{2, 2});
        queue.offer(new double[]{1, 1});
        queue.offer(new double[]{2, 2});
        System.out.println("Result: " + Arrays.toString(queue.getWindow(8)));
        queue.drop(1);
        System.out.println("Result: " + Arrays.toString(queue.getWindow(4)));
        queue.offer(new double[]{3, 3});
        queue.offer(new double[]{4, 4});
        queue.drop(1);
        System.out.println("Result: " + Arrays.toString(queue.getWindow(4)));
        Assert.assertArrayEquals(new double[] {3, 4, 0, 0}, queue.getWindow(4), 0.0);
    }
}
