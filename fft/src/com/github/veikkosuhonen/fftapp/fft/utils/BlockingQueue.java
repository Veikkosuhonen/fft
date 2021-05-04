package com.github.veikkosuhonen.fftapp.fft.utils;

public class BlockingQueue implements ChunkQueue {

    private final int blockSize;
    private final int capacity;
    private final double[] array;

    private boolean hasSpace = true;
    private int head = 0;
    private int tail = 0;
    private final Object mutex = new Object();

    public BlockingQueue(int capacity, int blockSize) {
        this.blockSize = blockSize;
        this.capacity = capacity;
        this.array = new double[blockSize * capacity];
    }

    @Override
    public boolean offer(double[] data) {
        if (data.length != blockSize) throw new IllegalArgumentException("data length "+data.length+" must equal block size "+blockSize);
        synchronized (mutex) {
            if (!hasSpace) return false;
            System.arraycopy(data, 0, array, tail * blockSize, blockSize);
            tail = (tail + 1) % capacity;
            if (tail == head) hasSpace = false;
            return true;
        }
    }

    @Override
    public double[] poll() {
        synchronized (mutex) {
            if (head == tail && hasSpace) return null;
            double[] result = new double[blockSize];
            System.arraycopy(array, head * blockSize, result, 0, blockSize);
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
                int available = blockSize * (tail - head);
                int size = available < n ? available : n;
                System.arraycopy(array, blockSize * head, result, 0, size);
            } else {
                int length1 = blockSize * (capacity - head);
                length1 = length1 < n ? length1 : n;
                int length2 = blockSize * tail;
                length2 = (length1 + length2) < n ? (tail * blockSize) : (n - length1);
                System.arraycopy(array, head, result, 0, length1);
                System.arraycopy(array, 0, result, length1, length2);
            }
        }
        return result;
    }
}
