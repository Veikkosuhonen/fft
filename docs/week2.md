# Week 2 report

During the second week I've implemented a very straightforward naive 
[DFT](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/main/java/com/github/veikkosuhonen/fftapp/fft/NaiveDFT.java), 
and a simple recursive 
[FFT](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/main/java/com/github/veikkosuhonen/fftapp/fft/FFT.java) 
using the Cooley-Tukey algorithm, which seems to be the most common one used.
I've also added a [Complex](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/main/java/com/github/veikkosuhonen/fftapp/fft/Complex.java)-class 
for making complex number math easier. 

Basic unit-tests are in place for the DFT-classes and the Complex-class. 
For the [DFT-tests](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/test/java/FFTTest.java) (all in one test class currently), I am testing the correctness of the algorithms
by checking if they can calculate the frequency amplitudes of a generated test signal. I also compare their outputs to a FFT-algorithm provided by the Apache Commons Math library. 
I am aware that it's not really a valid method to test correctness of an algorithm by seeing if it matches a similar algorithm made by someone else, but it is quite helpful at this
point of development. The reference implementation is also a good baseline comparison for performance. Some early testing hints that my simple FFT implementation
is around 10% slower than the reference at small sample sizes. With larger samples the difference quickly drops below margin of error.

Performance benchmarking is still a WIP. I've started implementing a Benchmark-class 
to do a configurable number of rounds and warm-up rounds with the algorithms and compare their average runtimes with different sized samples. 
I'm planning to have an option for the benchmark suite to visualize the results using the Java Swing library and JFreeChart.

![FT of signals with periods 1, 10 and 100](https://github.com/Veikkosuhonen/fft/blob/main/docs/FT_plot.png)

I've also written documentation for most of the algorithms and utility classes, configured jacoco for test coverage and started using checkstyle.

### Application (not really related to the algorithms)

The graphical application also has made some progress, and it already accomplishes the desired result in a very basic form: 
it plays a wav-file and draws the frequency spectrum of an audio chunk each frame on the screen. There's a few practical issues with the current implementation however. 

First some pain with OpenGL, I couldn't get the program to write the frequency data to a 1d texture, which the shader program could read, and I also couldn't get
a Uniform Buffer Object to work, so I am using a uniform array which is limited to 1024 float values. I would like to have the frequency resolution be much larger in the future,
so I will either have to learn to properly use opengl textures or UBOs, or draw some mesh to represent the frequency values. 1d texture would be the best option because
then I could borrow some beautiful shaders from [ShaderToy](https://www.shadertoy.com/), as it's shader api uses 1d textures to send frequencies to the program. 
Overall I think this is quite trivial however. 

Another challenge I have with the application is that I have a thread for playing audio, and the render thread. 
Currently, the animation looks very choppy, since the audio-thread updates the signal buffer of the FFT much less frequently than the scene is rendered. I have already figured out
a somewhat unsatisfying solution to this with another app I've used for testing: 
the audio thread splits it's data at each update into chunks and adds them to a BlockingQueue. 
The render thread then iterates through the first `N` of those chunks in the queue and concats them into a signal window, from which the Fourier Transform is calculated. 
Now, because a BlockingQueue has a fixed capacity, the render thread will also remove a number of the oldest chunks from the queue, so that the audio thread can add more.

Because the threads run at very different speeds, its a bit tricky to figure out how many chunks the render thread should remove. Also, the data in the queue should be fairly recent
so that the FT doesn't lag behind the audio. I really feel like there must be some clean solution to this. It's some form of a producer consumer pattern, with the exception that
the render thread as a consumer is not required to consume all of the data, 
only some amount of sufficiently recent data, and that neither the producer or the consumer should ever have to wait.

Next week: proper benchmark suite, a more advanced FFT-algorithm, perhaps taking advantage of the fact that the audio signal only contains real values. 
Also the Cooley-Tukey algorithm can be improved by using bit manipulation to get rid of recursion. The FFT wikipedia page will be my main guide.

This week's working hours = 10h
