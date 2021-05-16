# User Guide

### Running the application

You will be shown an option dialog to select the audio input method, either audio file or microphone. 
If you pick the file option, file chooser dialog prompting you to select either a WAV or an MP3 file to be played will be shown. 
If you want to skip the dialog, you can set the environment variable AUDIO_FILE to point to the desired file.

WAV files can be played as is as they are in the PCM format, but MP3 files need to be converted. The application will attempt to convert a selected MP3 file 
with [ffmpeg](https://www.ffmpeg.org/), using the command `ffmpeg -i inputfile.mp3 PROJECT_ROOT/fftapp_temp.wav`, and then play the converted file `fftapp_temp.wav`.
If ffmpeg is not installed and included in PATH, this will fail. 

There maybe problems with audio devices on some Linux PCs unfortunately.

### Running benchmarks

Run the benchmark with `./gradlew benchmark` or `gradle benchmark`. This will take a few seconds and finally show a plot of the results.

### Building from source

To build the desktop application, run `gradle dist`. Builds are generated to jar `desktop/build/libs/desktop-1.0.jar`. 
Note that Gradle version >= 7.0 is incompatible, the provided wrapper should be used.

### Tests

To run unit tests, `gradle test`
