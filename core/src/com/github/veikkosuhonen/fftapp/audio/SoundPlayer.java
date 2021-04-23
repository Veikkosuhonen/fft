package com.github.veikkosuhonen.fftapp.audio;

import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.dct.FastDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.NaiveDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.RealOnlyDFT;
import com.github.veikkosuhonen.fftapp.fft.dft.InPlaceFFT;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import javax.sound.sampled.*;

/**
 * SoundPlayer
 */
public class SoundPlayer {
    private File audioFile;
    private Thread sourceThread;
    private Queue<double[]> queue;
    private int chunkSize;
    private byte[] audioBytes;

    public SoundPlayer(File audioFile, int chunkSize) {
        this.audioFile = audioFile;
        this.chunkSize = chunkSize;
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

    public void setOutput(Queue<double[]> queue) {
        this.queue = queue;
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