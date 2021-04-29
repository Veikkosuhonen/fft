# Implementation

## Project architecture
![architecture](https://github.com/Veikkosuhonen/fft/blob/main/docs/architecture.png)

The discrete fourier transform and discrete cosine transform algorithms are implemented and tested in the fft module, along with some utility classes.
The application with the rendering, UI, sound playing and processing logic is implemented in the core module, where DCT algorithms and methods from the ArrayUtils-class are used. 

### The DFT and FFT algorithms

TODO basic explanation. I admit the mathematical background is quite difficult for me to properly understand, but I hope I can vaguely explain the basic idea and why
FFT is so _Fast_.

## Application dataflow
![dataflow](https://github.com/Veikkosuhonen/fft/blob/main/docs/dataflow.png)

Originally I intended to contain all the relevant algorithmic code in the fft-module, but the application logic grew quite complex, so I feel like it is important 
to give an overview of this process of reading audio data from a file or the microphone to rendering a spectrogram on the screen. 

Quick walkthough of the relatively messy code. 
1. In the FFTApp create-method, a DCTProcessor and a SoundPlayer are instantiated with required params, alongside an 
ArrayBlockingQueue, which acts as a one way data channel from the SoundPlayer to the DCTProcessor. 
2. The SoundPlayer's start-method creates and runs a thread, which
opens an AudioInputStream to the audio file, and a SourceDataLine for the audio playback. 
3. PCM audio-data is read from the inputstream in a while loop, and is
converted to chunks of double arrays, which are appended to the queue. 
4. In the render thread, each frame, the DCTProcessor method getLeftRightDCT is called. This iterates through the first n chunks in the queue, where n is specified by
WINDOW_LENGTH, and builds a "window", a double array of length WINDOW_LENGTH * CHUNK_SIZE for left and right channels (In dual channel PCM audio format, even members
are the left channel and uneven the right channel).
5. The discrete cosine transform is calculated for the windows of both channels, by calling twice the process-method of the DCT-object given to the DCTProcessor. The 
calculated dct-values are returned in a double array.
6. In the render-method, some preprocessing is done on the dct-values. For example, only the first n frequencies need to be rendered (where n is SPECTRUM_LENGTH) as
most of the "action" in music and other audio happens in the lower frequencies. Some normalization and smoothening (averaging neighbouring values) 
may also need to be done.
7. Previously an array of floats containing the frequency data was directly sent to the shader program to be visualized, but in the current version, a spectrogram
or a time-frequency representation image is rendered instead. This is done by writing the dct values into the left edge of a texture, and shifting the entire
texture right each frame, so it is gradually filled. (This is actually horribly inefficient as the entire texture has to be uploaded to the GPU memory every frame, even when only a fraction of the data changes. But modern computers are too forgiving for me to bother myself implementing this properly with separate framebuffers)
8. The shader programs are in the assets directory under the core module. `texture.frag` is the one used for rendering.
