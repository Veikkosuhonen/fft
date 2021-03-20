# Week 1 report

During the first week I've chosen the project topic and designed the basic application structure. 

I have not made a decision what the final application will be like exactly 
but for now it's goal in short is to stream and play audio from a waveform file, 
calculate the discrete Fourier transform for each fixed size chunk in real time, and display the frequencies as a dynamic histogram.

It is the first time for me using the LibGDX-framework (I have experience with other game and graphics frameworks)
but it seems that it is very easy to separate the rendering and application logic from the actual algorithms, 
which will make testing and code-reviewing very easy as all the code concerned can be kept in a separate module. 

I've also spent a considerable amount of time studying the actual topic, the Fourier transform and discrete Fourier transform algorithms.
My math background consists of upper secondary school math, two courses in analysis and one in linear algebra, so I should just barely be able to get a grasp of the topic. 
Luckily I've already found a lot of educational resources online.
From what I can tell implementing a basic working fast Fourier transform algorithm is not too big of a task, but understanding it will take quite a bit of effort.
There also seems to be countless different optimizations and methods for the FFT algorithm, meaning that I probably wont run out of work during this course.

Next week my goal is to implement a test suite to validate a discrete Fourier transform algorithm in a few simple cases (for example calculating the frequency of a single sine wave) and
another test suite to test the speed. After the validation tests are ready I also plan to implement a simple naive DFT. If I have extra time I will work on a simple FFT algorithm 
and the application rendering and audio streaming.
