# Design Document

The main part of this project is implementing a [Fast Fourier Transform](https://en.wikipedia.org/wiki/Fast_Fourier_transform) algorithm to compute the frequency decomposition of a digital signal, specifically PCM-waveform audio. 
As there are myriads of different ways to implement FFT, a few of them will be implemented and compared to each other and to a baseline naive DFT-algorithm to see which methods perform best in this specific domain.

### Problem formulation

Given an array of bytes representing an audio signal, how to efficiently calculate the discrete fourier transform of the signal? 
The implementation should accomplish this in `O(n log(n))` time complexity as opposed to a naive implementation with `O(n^2)` time complexity.
Compare different efficient algorithms to find which performs best in practise.

### Practical information regarding the course

My degree is BSc in computer science, the programming language is Java, the documentation is in English.
