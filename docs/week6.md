# Weekly report 6

I've made pretty good progress this week and the project is now very close to completion. 
I've started drawing some lines as to what parts I will simply declare finished and not change those anymore, and add some details in the implementation document as to what I think they are lacking.

Starting with testing, unit testing is quite complete, with testing for DCT and DFT algorithms in place along the utility classes. 
Coverage is good but the algorithms could be tested in a smarter and more analytic manner. 
I will see if I have time to revise some of the algorithm tests but for now they are sufficient. 
Performance testing is also fairly complete for both tasks. 
Currently in the benchmark, all the DCT and DFT algorithms, except for the slow naive ones, are tested with sample sizes from 2^8 to 2^16 and the results are printed to console and plotted (with my quick JFreeChart wrapper code). 
I will include the results in a nice format in the testing document.

For the DFT algorithms, I've added a new implementation, [OptimizedInPlaceFFT](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/com/github/veikkosuhonen/fftapp/fft/dft/OptimizedInPlaceFFT.java) relying on in-place computation but without using the Complex-class. 
Turns out using my Complex-number objects had a quite significant effect on performance, and dropping them, with the expense of some readability, reduced computation time around 50% at very large sample sizes. 
Another optimization that I added was the precomputation and caching of the bit-reversal permutation, which is used in reordering the input array in-place, and the roots-of-unity -complex numbers used in the FFT-algorithm. 
The in-place FFT algorithm has `n/2 * log(n)` processing steps, and with caching, there is at least 1 complex multiplication or 4 64-bit float multiplications less for each iteration so the improvement is significant. 
The caching is only effective when the algorithm is called with the same input size, and I think it was a valid assumption to make since the application never changes the input size. 
Compared to the reference algorithm, which calls the Apache Commons Math -librarys fft-method, my fastest implementation is still around 30-40% slower.

The DCT implementation which uses a DFT algorithm, DCTDFT (terrible name), has now been "fixed", and now matches the pure DCT implementations. 
Mostly due to my poor understanding of the algorithm, I naively believed that I had to simply pass the values straight into the DFT, but turns out some `O(n)` preprocessing steps were needed for reordering the input array. 
Benchmarking also revealed that it is much faster to use an efficient DFT algorithm to compute the DCT instead of using a pure DCT algorithm, even if some additional preprocessing is needed. 
I guess that shows that for this specific application, a DCT algorithm adds no value over a DFT algorithm.

The application has again gone through some (mostly visual) changes but the core process is still the same. I've added some logic to normalize the frequency data based on average maximum amplitudes. The audio player can now also take audio from the microphone (hardcoded boolean value, probably only works on a similar windows pc as mine).
I don't remember if I added it last week or this week but the application now renders a time-frequency spectrogram along the usual frequency bars. 
I've tried to document the main application class to explain the rendering logic but I guess that is not too important for the course.

About documentation, I've continued adding documentation as the codebase has expanded but unfortunately I still don't have complete documentation for some of the core algorithms. 
This is partly due to the biggest problem with my project: I don't have a good enough understanding of the more advanced FFT and FCT-algorithms in general, and that limits my ability to explain the code in sufficent detail. 
Documenting those will be my final big task of the project, besides finishing the implementation and testing documents.

Finally about implementing a synchronized queue for audio data chunks, which is a core part of the application. 
My own implementation _almost_ works as intended, but not quite, there is some small bugs to resolve. 
If I don't manage to resolve those bugs and match the Java library implementation, I will have to use the ArrayBlockingQueue from Java. 
The specialized queue data structure is defined in [ChunkQueue](https://github.com/Veikkosuhonen/fft/blob/main/fft/src/com/github/veikkosuhonen/fftapp/fft/utils/ChunkQueue.java) and its implementations are in the same `util` package.

I've worked around 15 hours this week.
