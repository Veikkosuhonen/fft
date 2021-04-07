package com.github.veikkosuhonen.fftapp.audio;

import com.github.veikkosuhonen.fftapp.fft.ArrayUtils;
import com.github.veikkosuhonen.fftapp.fft.DFT;
import com.github.veikkosuhonen.fftapp.fft.FFT;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * SoundPlayer
 */
public class SoundPlayer {
    private File file;
    private Thread sourceThread;
    ArrayBlockingQueue<double[]> queue;
    int chunkSize;
    int bufferSize;
    byte[] audioBytes;
    int queue_length = 256;
    int window = 128;
    int pollRate;
    DFT fft;

    public SoundPlayer(String filePath, int chunkSize, int bufferSize, int fps) {
        fft = new FFT();
        file = new File(filePath);
        queue = new ArrayBlockingQueue<>(queue_length);
        this.chunkSize = chunkSize;
        this.bufferSize = bufferSize;
        pollRate = (int)( 44100.0 / (fps * chunkSize / 2) );
    }

    public double[] getDFT() {
        double[] inReal = new double[window * chunkSize];
        //byte[] inImag = new byte[window * chunkSize / 2];

        Iterator<double[]> iter = queue.iterator();
        int i = 0;
        while (iter.hasNext() && i < window) {
            System.arraycopy(iter.next(), 0, inReal, i * chunkSize, chunkSize);
            i++;
        }

        //System.out.println(pollRate);
        for (int j = 0; j < pollRate; j++) queue.poll();
        return fft.process(new double[][]{inReal, new double[inReal.length]})[0];
    }

    public double[][] getLeftRightDFT() {
        double[] inLeft = new double[window * chunkSize / 2];
        double[] inRight = new double[window * chunkSize / 2];
        //byte[] inImag = new byte[window * chunkSize / 2];

        Iterator<double[]> iter = queue.iterator();
        int i = 0;
        while (iter.hasNext() && i < window) {
            double[] chunk = iter.next();
            for (int j = 0; j < chunk.length; j += 2) {
                inLeft[j / 2 + i * chunkSize / 2] = chunk[j];
                inRight[j / 2 + i * chunkSize / 2] = chunk[j + 1];
            }
            i++;
        }

        //System.out.println(pollRate);
        for (int j = 0; j < pollRate; j++) queue.poll();

        return new double[][] {
                fft.process(new double[][]{inLeft, new double[inLeft.length]})[0],
                fft.process(new double[][]{inRight, new double[inRight.length]})[0]};
    }

    public void start() {
        try {
            final AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open();

            sourceThread = new Thread() {
                @Override
                public void run() {
                    int bytesPerFrame = stream.getFormat().getFrameSize();
                    int numBytes = bufferSize * bytesPerFrame;
                    audioBytes = new byte[numBytes];
                    int bytesRead;
                    try {
                        sourceLine.start();
                        //long start = System.currentTimeMillis();
                        while ((bytesRead = stream.read(audioBytes, 0, numBytes)) != -1) {
                            double[] chunk = new double[chunkSize];
                            for (int i = 1; i < bytesRead / 2 && i < numBytes / 2; i++) {
                                chunk[i % chunkSize] = audioBytes[2 * i - 1];
                                if (i % chunkSize == chunkSize - 1) {
                                    queue.offer(chunk);
                                    chunk = new double[chunkSize];
                                }
                            }
                            sourceLine.write(audioBytes, 0, bytesRead);
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    sourceLine.stop();
                    sourceLine.close();
                }
            };

            sourceThread.start();

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException lue) {lue.printStackTrace();}
    }
    public void stop() {
        sourceThread.interrupt();
    }
}