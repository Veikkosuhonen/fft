package com.github.veikkosuhonen.fftapp.fft.utils;

/**
 * Simple offer-poll fixed size queue holding float values
 */
public class FloatQueue {
    private final float[] array;
    private final int capacity;
    private int size;
    private int head;
    private int tail;
    private boolean hasSpace;

    /**
     * Constructs a new FloatQueue with given size
     * @param capacity the capacity of the queue
     */
    public FloatQueue(int capacity) {
        this.capacity = capacity;
        this.array = new float[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
        this.hasSpace = true;
    }

    /**
     * Attempts to add a float value to the queue. If the queue is full, value is not added.
     * @param value the float value
     * @return whether the value could be added
     */
    public boolean offer(float value) {
        if (!hasSpace) return false;
        array[tail] = value;
        tail = (tail + 1) % capacity;
        size += 1;
        if (tail == head) hasSpace = false;
        return true;
    }

    /**
     * Attempts to poll the first element from the head queue, removing it and returning its value. If the queue
     * is empty, returns 0f and the queue is not changed.
     * @return the polled element or 0f if the queue is empty
     */
    public float poll() {
        if (tail == head && hasSpace) return 0f;
        float result = array[head];
        head = (head + 1) % capacity;
        size -= 1;
        hasSpace = true;
        return result;
    }

    /**
     * @return How many elements are in the queue
     */
    public int size() {
        return this.size;
    }
}
