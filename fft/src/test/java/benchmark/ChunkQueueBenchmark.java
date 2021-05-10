package benchmark;

import com.github.veikkosuhonen.fftapp.fft.utils.BlockingQueue;
import com.github.veikkosuhonen.fftapp.fft.utils.ChunkQueue;
import com.github.veikkosuhonen.fftapp.fft.utils.ReferenceChunkQueue;

public class ChunkQueueBenchmark {

    static int TRIALS = 100;
    static int WARMUP = 50;
    static int CAPACITY = 1024;
    static int CHUNK_SIZE = 128;
    static int ITERS = 100_000;
    static double[] chunk = new double[CHUNK_SIZE];

    public static void main(String[] args) {
        Thread producer;
        Thread consumer;

        // benchmark reference
        long avgTime = 0L;
        for (int i = 0; i < TRIALS + WARMUP; i++) {
            final ChunkQueue queue = new ReferenceChunkQueue(CAPACITY, CHUNK_SIZE);

            producer = new Thread(() -> {
                for (int j = 0; j < ITERS; j++) {
                    queue.offer(chunk);
                }
            });
            consumer = new Thread(() -> {
                for (int j = 0; j < ITERS; j++) {
                    double[] window = queue.getWindow(444);
                    queue.drop(123);
                }
            });

            long start = System.currentTimeMillis();
            producer.start();
            consumer.start();
            try { producer.join(); consumer.join(); } catch (InterruptedException ie) { ie.printStackTrace(); }
            long dur = System.currentTimeMillis() - start;

            if (i > WARMUP) avgTime += dur;
        }
        System.out.println("Reference: " + avgTime / TRIALS + " ms");

        // benchmark custom
        avgTime = 0L;
        for (int i = 0; i < TRIALS + WARMUP; i++) {
            final ChunkQueue queue = new BlockingQueue(CAPACITY, CHUNK_SIZE);

            producer = new Thread(() -> {
                for (int j = 0; j < ITERS; j++) {
                    queue.offer(chunk);
                }
            });
            consumer = new Thread(() -> {
                for (int j = 0; j < ITERS; j++) {
                    double[] window = queue.getWindow(444);
                    queue.drop(123);
                }
            });

            long start = System.currentTimeMillis();
            producer.start();
            consumer.start();
            try { producer.join(); consumer.join(); } catch (InterruptedException ie) { ie.printStackTrace(); }
            long dur = System.currentTimeMillis() - start;

            if (i > WARMUP) avgTime += dur;
        }
        System.out.println("Custom: " + avgTime / TRIALS + " ms");
    }
}
