package com.github.veikkosuhonen.fftapp.fft.utils;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public class ReferenceChunkQueue implements ChunkQueue {

    private ArrayBlockingQueue<double[]> queue;
    private int capacity;
    private int chunkSize;

    public ReferenceChunkQueue(int capacity, int chunkSize) {
        this.capacity = capacity;
        this.chunkSize = chunkSize;
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public boolean offer(double[] data) {
        if (data.length != chunkSize) throw new IllegalArgumentException("Wrong sized chunk (" + data.length + " != " + chunkSize + ")");
        return queue.offer(data);
    }

    @Override
    public double[] poll() {
        return queue.poll();
    }

    @Override
    public boolean drop(int n) {
        boolean success = true;
        for (int i = 0; i < n; i++)
            if (queue.poll() == null)
                success = false;
        return success;
    }

    @Override
    public int remainingCapacity() {
        return queue.remainingCapacity();
    }

    @Override
    public double[] getWindow(int n) {
        double[] result = new double[n];
        Iterator<double[]> iterator = queue.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            double[] chunk = iterator.next();
            for (int j = 0; j < chunkSize; j++) {
                int idx = i * chunkSize + j;
                result[idx] = chunk[j];
                if (idx == n - 1) return result;
            }
            i++;
        }
        return result;
    }
}
