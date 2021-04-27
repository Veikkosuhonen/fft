# Weekly report 5

I totally forgot to write a report for week 4 so some work from that week is considered here as well. 

Unfortunately there hasn't been very much progress on improving the FFT/FCT algorithms. I've instead focused on refactoring the software and added a couple QoL-features,
such as a file-chooser dialog and automatic mp3 to wav conversion using the ffmpeg tool. (Sadly it's way beyond the scope of this project to implement mp3-decoding)

I've also implemented a signal windowing functionality, which in essence scales the signal given to the algorithm with a [window-function](https://en.wikipedia.org/wiki/Window_function).
This should in theory be able to reduce signal-to-noise ratio with the right window function, but I have no other way of measuring the effect than just visually looking at it.

The project is getting closer to finish but there are a few things that are required. 
I need to implement from scratch a queue for the audio data transfer from audio thread to render thread. Currently an ArrayBlockingQueue is used. 
Minimum requirements are that the datastructure should be able to hold a fixed number of chunks (double arrays) 
and implement thread safe poll- and offer-methods. It should also be possible to atomically access the N first chunks in the queue, 
possibly by copying the contents of the queue into a new array.

More complete documentation for the DCT/DFT-algorithms is also required. The RealOnlyDFT also is not working the way it should. I need to wrap my head around the
mathematical difference between a DCT and a DFT, since while the calculation is quite similar, 
the end result - calculating the frequency amplitudes of a real signal - is similar but not the same. 

Unit-testing is in a fairly good state, the only problem is the disparity between some of the DCT-algorithms. Once that is solved I should be able to get a 100% coverage
on the relevant classes. The more complex application classes, such as the DCPProcessor, might not be tested thoroughly. 

Performance-testing is still insufficient, and there is no benchmarking for the DCT-algorithms yet. I'm also doing experiments with some minor optimizations 
and testing the effect of using Complex-objects.  
