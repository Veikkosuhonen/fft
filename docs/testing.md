# Testing

## Unit tests

The algorithms and utility classes in the project are unit tested. Code coverage report is generated with JaCoCo:
![](https://github.com/Veikkosuhonen/fft/blob/main/docs/jacoco_report.png)

The important DFT correctness tests are in [DFTTest](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/test/java/DFTTest.java). 
DFT algorithms are tested on sample sizes from 1 to 2048 on a generated signal with known non-resonating frequencies, making correctness easy to test. 
For example, when the signal has a period 3 sine wave, `dftResult[3] == 0.5`. 

## Performance testing

The performance of DFT- and DCT-algorithms are tested with different sample sizes and the results are plotted. 
Each algorithm is ran for 50 warmup rounds and 100 trial rounds for each sample size and the runtime average is saved.

![](https://github.com/Veikkosuhonen/fft/blob/main/docs/benchmark1.png)

Results for DFT and DCT algorithms with sample sizes (powers of two) on the X-axis and average runtime on the Y-axis (milliseconds)

Some textual benchmark results. Readers should see the implementation document and the corresponding classes in the `dft` module for details.

```
Sample size  = 64
Naive        = 0.078 ms
FFT          = 0.024 ms
InPlaceFFT   = 0.008 ms
FFT2         = 0.014 ms
ParallelFFT  = 0.021 ms
Optimized    = 0.004 ms
ReferenceFFT = 0.006 ms

Sample size  = 256
Naive        = 1.04 ms
FFT          = 0.017 ms
InPlaceFFT   = 0.013 ms
FFT2         = 0.012 ms
ParallelFFT  = 0.019 ms
Optimized    = 0.018 ms
ReferenceFFT = 0.011 ms
```
At small sample sizes the comparison fluctuates a lot and there is no clear winner. 
But it is clear that the naive DFT implementation quickly becomes extremely slow compared to the efficient algorithms. In larger benchmarks it is excluded. 
```
Sample size  = 16384
FFT          = 1.47 ms
InPlaceFFT   = 1.35 ms
ParallelFFT  = 0.44 ms
Optimized    = 0.32 ms
FFT2         = 1.05 ms
ReferenceFFT = 0.36 ms

Sample size  = 65536
FFT          = 6.46 ms
InPlaceFFT   = 6.39 ms
ParallelFFT  = 1.88 ms
Optimized    = 1.52 ms
FFT2         = 4.70 ms
ReferenceFFT = 1.68 ms
```
At large sample sizes the scaling of the efficient algorithms is quite close to linear. 
