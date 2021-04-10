# Weekly raport 3

During week 3 (two weeks really if the easter week is counted, however I only worked 1 week), programming-wise I've worked a little bit on every part of the project. 

I've implemented a FFT-algorithm that gets rid of recursion using bit-reversing permutation ([InPlaceFFT](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/main/java/com/github/veikkosuhonen/fftapp/fft/dft/InPlaceFFT.java)). There is a lot of optimizations to be done and currently it performs exactly the same as the traditional recursive FFT.

I've also worked a bit on benchmarking, the benchmark suite now plots the runtimes of different FFT implementations depending on the sample size:![benchmark](https://user-images.githubusercontent.com/54055199/114269901-122d9c80-9a12-11eb-925b-52f9b0467084.png)
This plot made me notice how insufficient my benchmarks were previously, as it seemed that my implementations were about as good as the reference. (I claimed them to be 10% slower, but they are clearly around 5 times slower). To calculate these, I ran 50 warmup rounds and 100 trial rounds and averaged the runtimes of the trial rounds. Note that only sample sizes that are a power of two are used, since the implementations require that.

During this week I also realised that the task which im using these FFT algorithms for is actually not a Discrete Fourier Transform, but instead a Discrete Cosine Transform, which  calculates the frequencies of a purely real signal. I've therefore separeted the DFTs and DCTs into separate packages. For now the only DCT implementation [RealOnlyDFT](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/main/java/com/github/veikkosuhonen/fftapp/fft/dct/RealOnlyDFT.java) uses a DFT-algorithm, but a dedicated DCT algorithm should be able to outperform that. The benchmarking will take this into account and the suite will have separate comparisons for DFTs and DCTs.

The application itself has made some nice progress and is visually quite complete. ![app](https://user-images.githubusercontent.com/54055199/114270399-c3353680-9a14-11eb-963b-d714a5e9ad07.png)

256 of the lowest frequency values from left (top) and right (bottom) channels are passed into the fragment shader. I've also adapted another [really nice shader](https://www.shadertoy.com/view/ls3BDH) from ShaderToy, far beyond my artistic skills, for this application. ![image](https://user-images.githubusercontent.com/54055199/114270606-004df880-9a16-11eb-9cd1-94e0a258d818.png) 

Looks pretty but not too informative. To make this look more natural I will have to use logarithmic scaling on the frequencies, as a lot more is happening in the low frequencies than in the high ones.

My plans for for next week and the future. The application is almost complete, I just need to add a way to select audio files and have some playback control. For the algorithms, I will be looking at ways to make a DCT that might beat the traditional FFT at processing real-only signals. I'm also very interested in approximate solutions, that could outperform the exact DFT-algorithms in some cases. I also want to try a Discrete Wavelet Transform, which can be used to achieve the same results as a DFT. I also will look at optimizing the existing algorithms I have. For example I will have to benchmark the effect of using the Complex-class in the implementations.
