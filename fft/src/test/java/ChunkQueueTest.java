import com.github.veikkosuhonen.fftapp.fft.utils.BlockingQueue;
import com.github.veikkosuhonen.fftapp.fft.utils.ChunkQueue;
import com.github.veikkosuhonen.fftapp.fft.utils.ReferenceChunkQueue;
import org.junit.Assert;
import org.junit.Test;

public class ChunkQueueTest {

    static double[] chunk = new double[] {1, 2};

    @Test
    public void referenceSimpleOfferPollTest() {
        ChunkQueue queue = new ReferenceChunkQueue(1, 2);
        Assert.assertNull("Empty queue returns null on poll", queue.poll());
        Assert.assertTrue("Offer returns true when has capacity", queue.offer(chunk));
        Assert.assertFalse("Offer returns false when full", queue.offer(new double[2]));
        Assert.assertArrayEquals("Polled content equals", chunk, queue.poll(), 0.0);
        Assert.assertTrue("Capacity freed after poll", queue.offer(chunk));
    }

    @Test
    public void customChunkQueueSimpleOfferPollTest() {
        ChunkQueue queue = new BlockingQueue(1, 2);
        Assert.assertNull("Empty queue returns null on poll", queue.poll());
        Assert.assertTrue("Offer returns true when has capacity", queue.offer(chunk));
        Assert.assertFalse("Offer returns false when full", queue.offer(new double[2]));
        Assert.assertArrayEquals("Polled content equals", chunk, queue.poll(), 0.0);
        Assert.assertTrue("Capacity freed after poll", queue.offer(chunk));
    }

    @Test
    public void referenceSimpleCapacityTest() {
        ChunkQueue queue = new ReferenceChunkQueue(2, 2);
        Assert.assertEquals(2, queue.remainingCapacity());
        queue.offer(chunk);
        Assert.assertEquals(1, queue.remainingCapacity());
        queue.offer(chunk);
        Assert.assertEquals(0, queue.remainingCapacity());
        queue.offer(chunk);
        Assert.assertEquals(0, queue.remainingCapacity());
        queue.poll();
        Assert.assertEquals(1, queue.remainingCapacity());
    }

    @Test
    public void customSimpleCapacityTest() {
        ChunkQueue queue = new BlockingQueue(2, 2);
        Assert.assertEquals(2, queue.remainingCapacity());
        queue.offer(chunk);
        Assert.assertEquals(1, queue.remainingCapacity());
        queue.offer(chunk);
        Assert.assertEquals(0, queue.remainingCapacity());
        queue.offer(chunk);
        Assert.assertEquals(0, queue.remainingCapacity());
        queue.poll();
        Assert.assertEquals(1, queue.remainingCapacity());
    }

    @Test
    public void referenceSimpleDropTest() {
        ChunkQueue queue = new ReferenceChunkQueue(10, 2);
        for (int i = 0; i < 10; i++) {
            queue.offer(chunk);
        }
        Assert.assertEquals(0, queue.remainingCapacity());
        queue.drop(5);
        Assert.assertEquals(5, queue.remainingCapacity());
        queue.drop(5);
        Assert.assertEquals(10, queue.remainingCapacity());
    }

    @Test
    public void customSimpleDropTest() {
        ChunkQueue queue = new BlockingQueue(10, 2);
        for (int i = 0; i < 10; i++) {
            queue.offer(chunk);
        }
        Assert.assertEquals(0, queue.remainingCapacity());
        queue.drop(5);
        Assert.assertEquals(5, queue.remainingCapacity());
        queue.drop(5);
        Assert.assertEquals(10, queue.remainingCapacity());
    }

    @Test
    public void referenceSimpleWindowTest() {
        double[] window = new double[] {1, 2, 1, 2, 1};
        ChunkQueue queue = new ReferenceChunkQueue(10, 2);
        for (int i = 0; i < 10; i++) {
            queue.offer(chunk);
        }
        Assert.assertArrayEquals(window, queue.getWindow(5), 0.0);
    }

    @Test
    public void customSimpleWindowTest() {
        double[] window = new double[] {1, 2, 1, 2, 1};
        ChunkQueue queue = new BlockingQueue(10, 2);
        for (int i = 0; i < 10; i++) {
            queue.offer(chunk);
        }
        Assert.assertArrayEquals(window, queue.getWindow(5), 0.0);
    }

    @Test
    public void referenceComplexOfferPollTest() {
        // input data
        double[][] chunks = new double[20][2];
        for (int i = 0; i < 20; i++) {
            chunks[i] = new double[] {i, 2 * i};
        }

        ChunkQueue queue = new ReferenceChunkQueue(10, 2);
        for (int i = 0; i < 20; i++) { // offer 20, first 10 return true
            Assert.assertEquals(i < 10, queue.offer(chunks[i]));
        }
        for (int i = 0; i < 5; i++) { // poll 5, equal contents
            Assert.assertArrayEquals(chunks[i], queue.poll(), 0.0);
        }
        for (int i = 0; i < 10; i++) { // offer 10, first 5 return true
            Assert.assertEquals(i < 5, queue.offer(chunks[i + 10]));
        }
        // contents are now 5-15
        for (int i = 0; i < 10; i++) {
            Assert.assertArrayEquals(chunks[i + 5], queue.poll(), 0.0);
        }
    }

    @Test
    public void customComplexOfferPollTest() {
        // input data
        double[][] chunks = new double[20][2];
        for (int i = 0; i < 20; i++) {
            chunks[i] = new double[] {i, 2 * i};
        }

        ChunkQueue queue = new BlockingQueue(10, 2);
        for (int i = 0; i < 20; i++) { // offer 20, first 10 return true
            Assert.assertEquals(i < 10, queue.offer(chunks[i]));
        }
        for (int i = 0; i < 5; i++) { // poll 5, equal contents
            Assert.assertArrayEquals(chunks[i], queue.poll(), 0.0);
        }
        for (int i = 0; i < 10; i++) { // offer 10, first 5 return true
            Assert.assertEquals(i < 5, queue.offer(chunks[i + 10]));
        }
        // contents are now 5-15
        for (int i = 0; i < 10; i++) {
            Assert.assertArrayEquals(chunks[i + 5], queue.poll(), 0.0);
        }
    }

    @Test
    public void referenceComplexWindowTest() {
        double[] window = new double[] {4, 4, 5, 5, 6};
        ChunkQueue queue = new ReferenceChunkQueue(6, 2);
        for (int i = 0; i < 6; i++) {
            queue.offer(new double[]{i, i});
        }
        queue.drop(4);
        for (int i = 0; i < 4; i++) {
            queue.offer(new double[]{i + 6, i + 6});
        }
        Assert.assertArrayEquals(window, queue.getWindow(5), 0.0);
    }

    @Test
    public void customComplexWindowTest() {
        double[] window = new double[] {4, 4, 5, 5, 6};
        ChunkQueue queue = new BlockingQueue(6, 2);
        for (int i = 0; i < 6; i++) {
            queue.offer(new double[]{i, i});
        }
        queue.drop(4);
        for (int i = 0; i < 4; i++) {
            queue.offer(new double[]{i + 6, i + 6});
        }
        Assert.assertArrayEquals(window, queue.getWindow(5), 0.0);
    }
}
