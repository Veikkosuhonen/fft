# fft
DSA-course project which implements several Discrete Fourier Transform algorithms. 

The DFT implementations and tests are contained in the `fft` module. This is the important part regarding the DSA-course.

The desktop application is built on the [LibGDX game framework](https://libgdx.com/) to render the frequency spectrum of audio in real time. 
The implementation is in the `core` module.



## Docs

[User guide](https://github.com/Veikkosuhonen/fft/blob/main/docs/user_guide.md)

[Design document](https://github.com/Veikkosuhonen/fft/blob/main/docs/design_document.md)

[Implementation](https://github.com/Veikkosuhonen/fft/blob/main/docs/implementation.md)

[Testing](https://github.com/Veikkosuhonen/fft/blob/main/docs/testing.md)

### Weekly reports

[week 1](https://github.com/Veikkosuhonen/fft/blob/main/docs/week1.md)

[week 2](https://github.com/Veikkosuhonen/fft/blob/main/docs/week2.md)

[week 3](https://github.com/Veikkosuhonen/fft/blob/main/docs/week3.md)

[week 5](https://github.com/Veikkosuhonen/fft/blob/main/docs/week5.md)

## Gradle

`gradle run` to launch the desktop application. (Set `AUDIO_FILE` to point to a wav file if you want to skip the file-choosing dialog).

`gradle test` to run tests in the `fft` module. To read the Jacoco test report, open `fft/build/jacocoHtml/index.html`.

`gradle benchmark` to run benchmark suite and display results (very WIP)

`gradle check` to run checkstyle.

## Troubleshooting

If you get an error `Inconsistency detected by ld.so:...` when running the application on Linux, please make sure you are using JDK 8. You can explicitly specify it by adding the option `-Dorg.gradle.java.home=<path-to-jdk8>`. Currently audio may not work on Linux.
