package com.github.veikkosuhonen.fftapp.audio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Singleton class for abstracting the UI dialogs, audio file selection and possible conversion from the main application
 */
public class UI {

    private static final String TEMP_FILENAME = "fftapp_temp.wav";
    private static File audioFile;

    /**
     * Shows a dialog prompting user to either use the microphone or the audio file option
     * @return if user chose microphone
     */
    public static boolean useMicrophone() {
        Object[] options = {"Audio file", "Microphone"};
        int choice = JOptionPane.showOptionDialog(null,
                "Please select audio input method",
                "Audio input",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        return choice == 1;
    }

    /**
     * Prompts user to select a file and converts it from mp3 to wav if needed, and finally returns a wav file.
     * @return a wav file
     */
    public static File selectAudioFile() {
        chooseDefaultFile();
        if (audioFile == null) {
            chooseFile();
        }
        convertFile();
        return audioFile;
    }

    /**
     * By setting the environment variable AUDIO_FILE to a file path, you can skip the file chooser dialog
     */
    private static void chooseDefaultFile() {
        String path = System.getenv("AUDIO_FILE");
        if (path == null) {
            return;
        }
        audioFile = new File(path);
        if (!audioFile.exists()) {
            audioFile = null;
        }
    }

    /**
     * Shows a file chooser dialog until a WAV file is chosen
     */
    private static void chooseFile() {
        JPanel panel = new JPanel();
        panel.setSize(1024, 1024);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("Select");
        fileChooser.setDialogTitle("Select or drag an audio file to be played");
        fileChooser.setDragEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("wav, mp3", "wav", "mp3");
        fileChooser.setFileFilter(filter);
        while (true) {
            if (fileChooser.showDialog(panel, "Select") == JFileChooser.APPROVE_OPTION) {
                audioFile = fileChooser.getSelectedFile();
                break;
            }
        }
    }

    /**
     * Checks if file extension is mp3 and calls {@link FFmpegCaller} to convert the file to a temporary wav file.
     */
    private static void convertFile() {
        if (audioFile.getName().endsWith(".mp3")) {
            Logger.getAnonymousLogger().info("Converting " + audioFile.getName());
            String mp3Path = audioFile.getAbsolutePath();
            String wavPath = Paths.get("").toAbsolutePath().toString() + File.separator + TEMP_FILENAME;

            if (FFmpegCaller.convertToWav(mp3Path, wavPath)) {
                audioFile = new File(wavPath);
                audioFile.deleteOnExit();
                Logger.getAnonymousLogger().info("File converted");
            }

        }
    }
}
