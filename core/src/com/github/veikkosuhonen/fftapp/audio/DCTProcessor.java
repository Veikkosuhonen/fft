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

    public DCTProcessor(DCT dct, int chunkSize, int window, int fps, WindowFunction windowFunction, Queue<double[]> queue) {
        this.dct = dct;
        this.chunkSize = chunkSize;
        this.window = window;
        this.pollRate = (int)( 44100.0 / (fps * chunkSize / 2) );
        this.windowFunction = windowFunction;
        this.queue = queue;
    }

    /**
     * Calculates the DCT for the audio data in the queue over the window specified for the {@code SoundPlayer}
     * @return an array of two double arrays for the left and right channel DCT values
     */
    public double[][] getLeftRightDCT() {

        // Iterate over the first (window) chunks in the queue.
        // In stereo format, even members belong to the left channel and uneven to the right channel.
        Iterator<double[]> iter = queue.iterator();
        double[] dataL = new double[window * chunkSize / 2];
        double[] dataR = new double[window * chunkSize / 2];
        int i = 0;
        while (iter.hasNext() && i < window) {
            double[] chunk = iter.next();
            for (int j = 0; j < chunk.length / 2; j ++) {
                int index = j + i * chunkSize / 2;
                double coeff = windowFunction.getCoefficient(index);
                dataL[index] = chunk[j * 2] * coeff;
                dataR[index] = chunk[j * 2 + 1] * coeff;
            }
            i++;
        }

        // Remove (pollRate) chunks from the queue.
        // pollRate should be approximately the rate (per render frame) at which the audio thread adds chunks to the queue.
        for (int j = 0; j < pollRate; j++) queue.poll();

        return new double[][] {dct.process(dataL), dct.process(dataR) };
    }
}
