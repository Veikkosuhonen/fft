package com.github.veikkosuhonen.fftapp.audio;

import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.utils.ChunkQueue;
import com.github.veikkosuhonen.fftapp.fft.windowing.Hann;
import com.github.veikkosuhonen.fftapp.fft.windowing.Square;
import com.github.veikkosuhonen.fftapp.fft.windowing.WindowFunction;

public class DCTProcessor {

    private ChunkQueue queue;
    private DCT dct;
    private int chunkSize;
    private int window;
    private int channelWindowSize;
    private int pollRate;
    private WindowFunction windowFunction;

    public DCTProcessor(DCT dct, int chunkSize, int window, int fps, ChunkQueue queue) {
        this.dct = dct;
        this.chunkSize = chunkSize;
        this.window = window;
        this.pollRate = (int)( 44100.0 / (fps * chunkSize / 2) );
        this.channelWindowSize = window * chunkSize / 2;
        this.windowFunction = new Hann(channelWindowSize);
        this.queue = queue;
    }

    /**
     * Calculates the DCT for the audio data in the queue over the window specified for the {@code DCTProcessor}
     * @return an array of two double arrays for the left and right channel DCT values
     */
    public double[][] getLeftRightDCT() {

        // Iterate over the first (window) chunks in the queue.
        // In stereo format, even members belong to the left channel and uneven to the right channel.
        double[] dataWindow = queue.getWindow(window * chunkSize);
        double[] dataL = new double[channelWindowSize];
        double[] dataR = new double[channelWindowSize];

        for (int i = 0; i < channelWindowSize; i += 1) {
            double coeff = windowFunction.getCoefficient(i);
            dataL[i] = dataWindow[i * 2] * coeff;
            dataR[i] = dataWindow[i * 2 + 1] * coeff;
        }

        // Remove (pollRate) chunks from the queue.
        // pollRate should be approximately the rate (per render frame) at which the audio thread adds chunks to the queue.
        queue.drop(pollRate);

        return new double[][] { dct.process(dataL), dct.process(dataR) };
    }
}
