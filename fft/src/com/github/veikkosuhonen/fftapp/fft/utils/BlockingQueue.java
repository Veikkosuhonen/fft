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
            head = (head + n) % capacity;
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
        if (remainingCapacity() == capacity) return result;

        synchronized (mutex) {
            // the queue can be located in one continuous section in the array, or two sections: the first section
            // at the end of the array and the second section at the start.
            if (tail > head) {
                // one section
                int available = (tail - head - 1) * chunkSize;
                int length = n < available ? n : available;
                System.arraycopy(array, head * chunkSize, result, 0, length);
            } else {
                // two sections
                int available1 = (capacity - head) * chunkSize;
                if (n <= available1) {
                    // window fits in first section
                    System.arraycopy(array, head * chunkSize, result, 0, n);
                } else {
                    // both sections needed for window
                    int length1 = available1;
                    System.arraycopy(array, head * chunkSize, result, 0, length1);
                    int available2 = tail * chunkSize;
                    int length2 = n - length1;
                    length2 = length2 < available2 ? length2 : available2;
                    System.arraycopy(array, 0, result, length1, length2);
                }
            }
        }
        return result;
    }
}