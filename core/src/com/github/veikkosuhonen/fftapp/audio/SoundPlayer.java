package com.github.veikkosuhonen.fftapp.audio;

import com.github.veikkosuhonen.fftapp.fft.ArrayUtils;
import com.github.veikkosuhonen.fftapp.fft.DFT;
import com.github.veikkosuhonen.fftapp.fft.ReferenceFFT;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

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
    int bytesRead;
    int numBytes;
    int bufferSize;
    byte[] audioBytes;
    public float[] latest;
    ArrayDeque<double[]> queue;
    DFT fft;
    int max_queue_length = 8;

    public SoundPlayer(String filePath, int bufferSize) {
        file = new File(filePath);
        bytesRead = 0;
        this.bufferSize = bufferSize;
        this.latest = new float[bufferSize];
        queue = new ArrayDeque<>();
        fft = new ReferenceFFT();
    }

    public float[] getDFT() {
        double[] inReal = new double[max_queue_length * bufferSize / 2];
        Iterator<double[]> iter = queue.iterator();
        int i = 0;
        while (iter.hasNext()) {
            double[] chunk = iter.next();
            for (int j = 1; j < bufferSize; j += 2) {
                inReal[i * (bufferSize / 2) + j / 2] = chunk[j] / 16.;
            }
            i++;
        }
        //double[] real = fft.process(new double[][]{inReal, new double[inReal.length]})[0];
        return ArrayUtils.slice(ArrayUtils.toFloatArray(inReal), 1000);
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
                    int chunks = 4 * bytesPerFrame;
                    numBytes = chunks * bufferSize;
                    audioBytes = new byte[numBytes];
                    try {
                        sourceLine.start();
                        while ((bytesRead = stream.read(audioBytes)) != -1) {

                            for (int i = 0; i < chunks; i++) {
                                double[] chunk = new double[bufferSize];
                                for (int j = 0; j < bufferSize; j++) {
                                    chunk[j] = audioBytes[i * bufferSize + j] / (double) Byte.MAX_VALUE;
                                }

                                queue.add(chunk);
                                if (queue.size() > max_queue_length) {
                                    queue.removeFirst();
                                }
                            }
                            sourceLine.write(audioBytes, 0, numBytes);
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    sourceLine.stop();
                    sourceLine.close();
                }
            };

            sourceThread.start();

        } catch (LineUnavailableException lue) {lue.printStackTrace();}
        catch (UnsupportedAudioFileException uafe) {uafe.printStackTrace();}
        catch (IOException ioe) {ioe.printStackTrace();}
    }
    public void stop() {
        sourceThread.interrupt();
    }
}