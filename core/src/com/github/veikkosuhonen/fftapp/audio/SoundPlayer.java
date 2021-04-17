package com.github.veikkosuhonen.fftapp.audio;

import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.dct.FastDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.NaiveDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.RealOnlyDFT;
import com.github.veikkosuhonen.fftapp.fft.dft.InPlaceFFT;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import javax.sound.sampled.*;

/**
 * SoundPlayer
 */
public class SoundPlayer {
    private File audioFile;
    private Thread sourceThread;
    ArrayBlockingQueue<double[]> queue;
    int chunkSize;
    int bufferSize;
    byte[] audioBytes;
    int queue_length;
    int window;
    int pollRate;
    DCT dct;

    public SoundPlayer(File audioFile, int chunkSize, int queue_length, int window, int fps) {
        int n = chunkSize * window;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("chunkSize * window must be a power of two (was " + n + ")");
        }

        this.audioFile = audioFile;
        this.queue = new ArrayBlockingQueue<>(queue_length);
        this.chunkSize = chunkSize;
        this.window = window;

        this.dct = new FastDCT();
        this.pollRate = (int)( 44100.0 / (fps * chunkSize / 2) );
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
                inLeft[j / 2 + i * chunkSize / 2] = chunk[j];
                inRight[j / 2 + i * chunkSize / 2] = chunk[j + 1];
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

    /**
     * Creates the audio context and starts an audio thread, which reads bytes from the audio file,
     * adds data chunks to the queue to be processed by a DCT algorithm and writes the bytes to a source line to be played
     * by the audio system.
     */
    public void start() {
        try {
            final AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open();
            //System.out.println(Arrays.toString(sourceLine.getControls()));
            setMasterGain(sourceLine);

            sourceThread = new Thread() {
                @Override
                public void run() {
                    int bytesPerFrame = stream.getFormat().getFrameSize();
                    int numBytes = 1024 * bytesPerFrame;
                    audioBytes = new byte[numBytes];
                    int bytesRead;
                    try {
                        sourceLine.start();
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

    /**
     * Interrupts the audio thread
     */
    public void stop() {
        sourceThread.interrupt();
    }

    /**
     * Attempts to lower the volume of the given {@code SourceDataLine} if controls are supported. Without this
     * the playback is quite loud.
     * @param line
     */
    private void setMasterGain(SourceDataLine line) {
        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            gain.setValue(-10);
        } else if (line.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl volume = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
            volume.setValue(0.5f);
        } else {
            Logger.getAnonymousLogger().warning("Cannot control volume, audio may be loud.");
        }
    }
}