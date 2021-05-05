package com.github.veikkosuhonen.fftapp.fft.utils;

public class BlockingQueue implements ChunkQueue {

    private final int chunkSize;
    private final int capacity;
    private final double[] array;

    private boolean hasSpace = true;
    private int head = 0;
    private int tail = 0;
    private final Object mutex = new Object();

    /**
     * Constructs a BlockingQueue with specified capacity and chunk size
     * @param capacity how many elements the queue can hold at maximum
     * @param chunkSize the size of the elements (or chunks) in the queue
     */
    public BlockingQueue(int capacity, int chunkSize) {
        this.chunkSize = chunkSize;
        this.capacity = capacity;
        this.array = new double[chunkSize * capacity];
    }

    @Override
    public boolean offer(double[] data) {
        if (data.length != chunkSize) throw new IllegalArgumentException("data length "+data.length+" must equal chunk size "+ chunkSize);
        synchronized (mutex) {
            if (!hasSpace) return false;
            System.arraycopy(data, 0, array, tail * chunkSize, chunkSize);
            tail = (tail + 1) % capacity;
            if (tail == head) hasSpace = false;
            return true;
        }
    }

    @Override
    public double[] poll() {
        synchronized (mutex) {
            if (head == tail && hasSpace) return null;
            double[] result = new double[chunkSize];
            System.arraycopy(array, head * chunkSize, result, 0, chunkSize);
            head = (head + 1) % capacity;
            hasSpace = true;
            return result;
        }
    }

    @Override
    public boolean drop(int n) {
        synchronized (mutex) {
            if (head == tail && hasSpace) return false;
            head = (head + 1) % capacity;
            hasSpace = true;
            return true;
        }
    }

    @Override
    public int remainingCapacity() {
        if (!hasSpace) {
            return 0;
        } else if (tail >= head) {
            return capacity - tail + head;
        } else {
            return head - tail;
        }
    }

    @Override
    public double[] getWindow(int n) {
        double[] result = new double[n];
        int left = n;
        synchronized (mutex) {
            if (tail >= head && hasSpace) {
                int available = chunkSize * (tail - head);
                int size = available < n ? available : n;
                System.arraycopy(array, chunkSize * head, result, 0, size);
            } else {
                int length1 = chunkSize * (capacity - head);
                length1 = length1 < n ? length1 : n;
                int length2 = chunkSize * tail;
                length2 = (length1 + length2) < n ? (tail * chunkSize) : (n - length1);
                System.arraycopy(array, head, result, 0, length1);
                System.arraycopy(array, 0, result, length1, length2);
            }
        }
        return result;
    }
}
