package com.github.veikkosuhonen.fftapp.audio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Collections;
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
    ArrayDeque<byte[]> queue;
    int bufferSize;
    byte[] audioBytes;

    public SoundPlayer(String filePath, int bufferSize) {
        file = new File(filePath);
        bytesRead = 0;
        queue = new ArrayDeque<>();
        this.bufferSize = bufferSize;
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
                    numBytes = bufferSize * bytesPerFrame;
                    audioBytes = new byte[numBytes];
                    try {
                        sourceLine.start();
                        while ((bytesRead = stream.read(audioBytes)) != -1) {
                            for (int i = 0; i < bytesPerFrame; i++) {
                                byte[] chunk = new byte[bufferSize];

                                if ((i + 1) * bufferSize - i * bufferSize >= 0)
                                    System.arraycopy(audioBytes, i * bufferSize, chunk, 0, bufferSize);
                                if (queue.size() > 32) {
                                    queue.removeFirst();
                                }
                                queue.add(chunk);
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