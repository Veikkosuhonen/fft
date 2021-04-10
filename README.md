# fft
DSA-course project which implements several Discrete Fourier Transform algorithms. 

The DFT implementations and tests are contained in the `fft` module. This is the important part regarding the DSA-course.

The desktop application is built on the [LibGDX game framework](https://libgdx.com/) to render the frequency decomposition of audio in real time. 
The implementation is in the `core` module.



## Docs

[Design document](https://github.com/Veikkosuhonen/fft/blob/main/docs/design_document.md)

### Weekly reports

[week 1](https://github.com/Veikkosuhonen/fft/blob/main/docs/week1.md)

[week 2](https://github.com/Veikkosuhonen/fft/blob/main/docs/week2.md)

[week 3](https://github.com/Veikkosuhonen/fft/blob/main/docs/week3.md)

## Gradle

`gradle run` to launch the desktop application. (At the moment it is hardcoded to play `core/assets/Crystallized.wav`, which is included in the repo.)

`gradle test` to run tests in the `fft` module. To read the Jacoco test report, open `fft/build/jacocoHtml/index.html`.

`gradle benchmark` to run benchmark suite and display results (very WIP)

`gradle check` to run checkstyle.
