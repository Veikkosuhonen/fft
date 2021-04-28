package com.github.veikkosuhonen.fftapp.audio;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.logging.Logger;

import javax.sound.sampled.*;

/**
 * SoundPlayer
 */
public class SoundPlayer {
    private boolean useMicrophone;
    private File audioFile;
    private Queue<double[]> queue;
    private int chunkSize;
    private byte[] audioBytes;

    public SoundPlayer(File audioFile, int chunkSize, boolean useMicrophone, Queue<double[]> queue) {
        this.audioFile = audioFile;
        this.chunkSize = chunkSize;
        this.useMicrophone = useMicrophone;
        this.queue = queue;
    }

    /**
     * Creates the audio context and starts an audio thread, which reads bytes from the audio file,
     * adds data chunks to the queue to be processed by a DCT algorithm and writes the bytes to a source line to be played
     * by the audio system.
     */
    public void start() {
        new Thread(() -> {
            AudioFormat format;
            DataLine.Info info;
            TargetDataLine microphone = null;
            AudioInputStream stream = null;
            // Query and open input stream for audio file or targetdataline for microphone
            try {
                if (useMicrophone) {
                    format = new AudioFormat(44100.0f, 16, 2, true, true);
                    info = new DataLine.Info(TargetDataLine.class, format);
                    microphone = (TargetDataLine) AudioSystem.getLine(info);
                    microphone.open(format);
                    microphone.start();
                } else {
                    stream = AudioSystem.getAudioInputStream(audioFile);
                    format = stream.getFormat();
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException uafe) {
                uafe.printStackTrace();
                return;
            }
            // Open a sourceline for output
            info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine sourceLine;
            try {
                sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open();
            } catch (LineUnavailableException lue) {
                lue.printStackTrace();
                return;
            }
            setMasterGain(sourceLine);

            // Start reading in data
            int bytesPerFrame = format.getFrameSize();
            int numBytes = 1024 * bytesPerFrame;
            audioBytes = new byte[numBytes];
            int bytesRead;
            try {
                sourceLine.start();
                do {
                    bytesRead = useMicrophone ? microphone.read(audioBytes, 0, numBytes) : stream.read(audioBytes, 0, numBytes);
                    double[] chunk = new double[chunkSize];
                    for (int i = 1; i < bytesRead / 2 && i < numBytes / 2; i++) {
                        chunk[i % chunkSize] = audioBytes[2 * i - 1];
                        if (i % chunkSize == chunkSize - 1) {
                            queue.offer(chunk);
                            chunk = new double[chunkSize];
                        }
                    }
                    sourceLine.write(audioBytes, 0, bytesRead);
                } while (bytesRead != -1);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
            sourceLine.stop();
            sourceLine.close();
        }).start();
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