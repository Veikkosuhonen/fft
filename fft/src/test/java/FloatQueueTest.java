import com.github.veikkosuhonen.fftapp.fft.utils.FloatQueue;
import org.junit.Assert;
import org.junit.Test;

public class FloatQueueTest {
    @Test
    public void testFloatQueue() {
        FloatQueue queue = new FloatQueue(10);
        Assert.assertEquals(0, queue.size());
        for (int i = 0; i < 20; i++) {
            Assert.assertEquals("Can add 10 elements and no more",i < 10, queue.offer(i));
        }
        Assert.assertEquals(10, queue.size());
        for (int i = 0; i < 20; i++) {
            Assert.assertEquals("Can poll 10 elements and they are correct values", i < 10 ? (float) i : 0f, queue.poll(), 0);
        }
        Assert.assertEquals(0, queue.size());
        // Queue works correctly when many values are added and polled?
        Assert.assertTrue(queue.offer(-1));
        Assert.assertTrue(queue.offer(0));
        for (int i = 1; i < 100; i++) {
            Assert.assertTrue(queue.offer(i));
            Assert.assertEquals(3, queue.size());
            Assert.assertEquals(i - 2, queue.poll(), 0);
        }
    }
}
