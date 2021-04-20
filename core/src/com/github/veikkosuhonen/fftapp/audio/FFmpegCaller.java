package com.github.veikkosuhonen.fftapp.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Singleton class to call ffmpeg for converting to a wav file from mp3 file
 */
public class FFmpegCaller {

    public static boolean convertToWav(String inputPath, String outputPath) {

        // Delete temp file if it wasn't successfully deleted on previous run
        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            outputFile.delete();
        }

        String command = "ffmpeg -i \"" + inputPath + "\" " + outputPath;
        Logger.getAnonymousLogger().info("Running command '" + command + "'");

        boolean result = execute(command);
        if (!result) {
            Logger.getAnonymousLogger().severe("Failed to decode mp3-file. Make sure ffmpeg is available.");
        }
        return result;
    }

    private static boolean execute(String command) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException ioe) {
            Logger.getAnonymousLogger().severe(ioe.getLocalizedMessage());
            return false;
        }
        printOutput(process);
        return true;
    }

    private static void printOutput(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ioe) {
            Logger.getAnonymousLogger().warning("Failed to print ffmpeg output: " + ioe.getLocalizedMessage());
        }
    }
}
