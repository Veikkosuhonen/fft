package com.github.veikkosuhonen.fftapp.fft.utils;

/**
 * Specialized queue data structure which holds fixed size double-arrays or chunks. It offers synchronized methods
 * to access and update its contents similar to the Java BlockingQueue interface.
 */
public interface ChunkQueue {


    /**
     * Synchronized method that attempts to add a chunk of data to the tail of the queue. Does not block if the queue
     * has no space, instead returns false and does not update the contents. If chunk is added, return true.
     * @param chunk the data chunk
     * @return whether the data could be added
     */
    boolean offer(double[] chunk);

    /**
     * Synchronized poll method that returns the first element in the queue and null if the queue is empty.
     * @return The first element or null if empty
     */
    double[] poll();

    /**
     * Synchronized method that removes and deletes the first n elements from the head of the queue.
     * @param n how many elements to remove
     * @return Whether n elements could be removed
     */
    boolean drop(int n);

    /**
     * Returns the number of elements that can be added to the queue until it becomes full
     * @return the remaining capacity
     */
    int remainingCapacity();

    /**
     * Synchronized method to return a size n double array containing n elements in
     * the chunks starting from the head of the queue. If the chunks of the queue have less than n elements in them in
     * total, the remaining elements in the array will be zero.
     * @param n The length of the desired array (window)
     * @return an array containing the first n elements in the chunks of the queue.
     */
    double[] getWindow(int n);
}