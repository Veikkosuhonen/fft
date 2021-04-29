# User Guide

### Running the application

Run the application in project root directory with `./gradlew run` or `gradle run` if you have Gradle installed. 

You will be shown a file chooser dialog prompting you to select either a WAV or an MP3 file to be played. 
If the dialog is not shown, restart (happens on Mac sometimes for an unknown reason). 
If you want to skip the dialog, you can set the environment variable AUDIO_FILE to point to the desired file.

WAV files can be played as is as they are in the PCM format, but MP3 files need to be converted. The application will attempt to convert a selected MP3 file 
with [ffmpeg](https://www.ffmpeg.org/), using the command `ffmpeg -i inputfile.mp3 PROJECT_ROOT/fftapp_temp.wav`, and then play the converted file `fftapp_temp.wav`.
If ffmpeg is not installed and included in PATH, this will fail. 

Once (if) you got it running, maximize the window and enjoy :)

### Running benchmarks

Run the benchmark with `./gradlew benchmark` or `gradle benchmark`. This will take a few seconds and finally show a plot of the results.
