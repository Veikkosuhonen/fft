package com.github.veikkosuhonen.fftapp.audio;

import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.windowing.WindowFunction;

import java.util.Iterator;
import java.util.Queue;

public class DCTProcessor {

    private Queue<double[]> queue;
    private DCT dct;
    private int chunkSize;
    private int window;
    private int pollRate;
    private WindowFunction windowFunction;

    public DCTProcessor(DCT dct, int chunkSize, int window, int fps, WindowFunction windowFunction) {
        this.dct = dct;
        this.chunkSize = chunkSize;
        this.window = window;
        this.pollRate = (int)( 44100.0 / (fps * chunkSize / 2) );
        this.windowFunction = windowFunction;
    }

    /**
     * Calculates the DCT for the audio data in the queue over the window specified for the {@code SoundPlayer}
     * @return an array of two double arrays for the left and right channel DCT values
     */
    public double[][] getLeftRightDCT() {
        double[] inLeft = new double[window * chunkSize / 2];
        double[] inRight = new double[window * chunkSize / 2];

        // Iterate over the first (window) chunks in the queue.
        // In stereo format, even members belong to the left channel and uneven to the right channel.
        Iterator<double[]> iter = queue.iterator();
        int i = 0;
        while (iter.hasNext() && i < window) {
            double[] chunk = iter.next();
            for (int j = 0; j < chunk.length; j += 2) {
                int index = j / 2 + i * chunkSize / 2;
                double coeff = windowFunction.getCoefficient(index);
                inLeft[index] = coeff * chunk[j];
                inRight[index] = coeff * chunk[j + 1];
            }
            i++;
        }

        // Remove (pollRate) chunks from the queue.
        // pollRate should be approximately the rate (per render frame) at which the audio thread adds chunks to the queue.
        for (int j = 0; j < pollRate; j++) queue.poll();

        return new double[][] {
                dct.process(inLeft),
                dct.process(inRight)};
    }

    public void setInput(Queue<double[]> queue) {
        this.queue = queue;
    }
}
