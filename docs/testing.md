# Testing

## Unit tests

The algorithms and utility classes in the project are unit tested. Code coverage report is generated with JaCoCo:
![](https://github.com/Veikkosuhonen/fft/blob/main/docs/jacoco_report.png)

## Performance testing

The performance of DFT- and DCT-algorithms are tested with different sample sizes and the results are plotted. 
Each algorithm is ran for 50 warmup rounds and 100 trial rounds for each sample size and the runtime average is saved.

![](https://github.com/Veikkosuhonen/fft/blob/main/docs/benchmark1.png)

A quick look at the results with the 5 discrete Fourier transform algorithms implemented in the project. The naive implementation runs in `O(n^2)` time and the efficient implementations in `O(n*log(n))`. 
The FFT implementation is a straightforward recursive version of the Cooley-Tukey -algorithm, and the InPlaceFFT and Optimized FFT use a variant of it which reorders the input data and does the computation in-place without recursive calls. 

```
Sample size  = 32
Naive        = 0.076 ms
FFT          = 0.025 ms
InPlaceFFT   = 0.015 ms
Optimized    = 0.015 ms
ReferenceFFT = 0.011 ms

Sample size  = 256
Naive        = 1.32 ms
FFT          = 0.016 ms
InPlaceFFT   = 0.037 ms
Optimized    = 0.011 ms
ReferenceFFT = 0.0049 ms
```
This already shows that the naive DFT implementation quickly becomes extremely slow compared to the efficient algorithms. In larger benchmarks it is excluded. 
```
Sample size  = 16384
FFT          = 1.5 ms
InPlaceFFT   = 1.7 ms
Optimized    = 0.61 ms
ReferenceFFT = 0.32 ms

Sample size  = 65536
FFT          = 7.0 ms
InPlaceFFT   = 6.8 ms
Optimized    = 3.1 ms
ReferenceFFT = 1.7 ms
```
As can be seen from the results, at very large sample sizes the scaling of the efficient algorithms is quite close to linear. 
The FFT and InPlaceFFT are tied, hinting that the effect of the recursive calls is quite minimal for performance. 
What differentiates them from the Optimized version is the use of Complex-objects, which improves readability but imposes significant object and function calling overhead. The Optimized version also caches some computation results to improve performance on calls with the same sample size.
