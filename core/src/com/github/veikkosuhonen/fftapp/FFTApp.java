package com.github.veikkosuhonen.fftapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import com.github.veikkosuhonen.fftapp.audio.AudioFile;
import com.github.veikkosuhonen.fftapp.audio.DCTProcessor;
import com.github.veikkosuhonen.fftapp.audio.SoundPlayer;

import com.github.veikkosuhonen.fftapp.fft.dct.DFTDCT;
import com.github.veikkosuhonen.fftapp.fft.dft.OptimizedInPlaceFFT;
import com.github.veikkosuhonen.fftapp.fft.utils.ArrayUtils;
import com.github.veikkosuhonen.fftapp.fft.utils.CustomChunkQueue;
import com.github.veikkosuhonen.fftapp.fft.utils.ChunkQueue;

import java.util.ArrayDeque;
import java.util.Queue;

import static com.badlogic.gdx.Gdx.*;

public class FFTApp extends ApplicationAdapter {

	/*
	* Number of pcm-values in each chunk in the queue.
	*/
	final int CHUNK_SIZE = 128;

	/*
	* Max capacity of the queue in chunks. Determines latency of the visualization alongside CHUNK_SIZE.
	*/
	final int QUEUE_LENGTH = 360;

	/*
	* How many chunks in a window. There's a frequency and temporal resolution tradeoff: larger value allows higher
	* resolution DCT to be calculated but is less responsive temporally and vice versa. Must be less than QUEUE_LENGTH,
	* and WINDOW * CHUNK_SIZE must be a power of two.
	*/
	final int WINDOW = 64;

	/*
	* The desktop application is targeting 60 fps. Important for determining queue poll rate.
	*/
	final int FPS = 60;

	/*
	* Length of the frequency spectrum (per channel) rendered.
	*/
	final int SPECTRUM_LENGTH = 683;


	/**
	 * How many previous frames the averaged normalization takes into account
	 */
	final int normalizeLag = 32;

	/**
	 * Magic value. Should be less than normalizeLag, little bit lower looks nice. Lower value results in less clipping
	 * but less dynamic range for the colors.
	 */
	final float normalizeFactor = 28.0f;

	/**
	 * The queue that holds maximum values of each frame used in averaged normalization
	 */
	Queue<Float> maxValue;

	/**
	 * The average maximum value of the last normalizeLag frames
	 */
	float avgMax;

	ChunkQueue queue;
	SoundPlayer player;
	DCTProcessor processor;

	ShaderProgram shader;
	Mesh mesh;


	/**
	 * Called when GDX application starts
	 */
	@Override
	public void create() {

		initializeGraphics();
		createPixmapTexture();

		maxValue = new ArrayDeque<>(normalizeLag);
		avgMax = 0.0f;

		queue = new CustomChunkQueue(QUEUE_LENGTH, CHUNK_SIZE);

		processor = new DCTProcessor(
				new DFTDCT(new OptimizedInPlaceFFT()),
				CHUNK_SIZE,
				WINDOW,
				FPS,
				queue
		);

		player = new SoundPlayer(
				AudioFile.get(),
				CHUNK_SIZE,
				true,
				queue
		);
		player.start();
	}


	/**
	 * Called each frame
	 */
	@Override
	public void render() {

		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		double[][] data = processor.getLeftRightDCT();

		//float[] dataL = processFrequencyData(data[0]);
		float[] dataR = processFrequencyData(data[1]);
		//float[] dataLR = ArrayUtils.join(dataL, dataR);

		updatePixmap(dataR);

		shader.bind();
		shader.setUniformf("u_resolution", graphics.getWidth(), graphics.getHeight());
		shader.setUniformi("spectrogram", 0);

		mesh.render(shader, GL20.GL_TRIANGLES);
	}


	@Override
	public void dispose() {
		shader.dispose();
		mesh.dispose();
		player.dispose();
	}


	/**
	 * Prepares the data for rendering by slicing, converting to float and absolute values, applying smoothing,
	 * and normalization.
	 * @param data
	 * @return preprocessed data
	 */
	private float[] processFrequencyData(double[] data) {
		if (data.length < SPECTRUM_LENGTH) {
			throw new IllegalArgumentException("DCT data should not be less than SPECTRUM_LENGTH (" + data.length + " < " + SPECTRUM_LENGTH + ")");
		}
		// Slice from the range (2, SPECTRUM_LENGTH + 2), convert to float, convert to absolute values
		float[] result = ArrayUtils.abs(ArrayUtils.toFloatArray(ArrayUtils.slice(data, 2, SPECTRUM_LENGTH + 2)));

		//Smooth: slightly average neighboring values.
		for (int i = 1; i < SPECTRUM_LENGTH - 1; i++) {
			result[i] = (result[i - 1] + 2 * result[i] + result[i + 1]) / 4;
		}

		// calculate the average max for normalization
		float max = ArrayUtils.max(result);
		maxValue.add(max);
		avgMax += max;
		if (maxValue.size() >= normalizeLag) {
			avgMax -= maxValue.remove();
		}
		// return the normalized data
		return ArrayUtils.scale(result, 0, avgMax / normalizeFactor, 0, 1);
	}


	/**
	 * Compile shader program and create the screen draw quad
	 */
	private void initializeGraphics() {
		shader = new ShaderProgram(Gdx.files.internal("test.vert"), Gdx.files.internal("texturebars.frag"));
		shader.bind();

		mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
		mesh.setVertices(new float[]
					{-1f, -1f, 0f,
					1f, -1f, 0f,
					1f, 1f, 0f,
					-1f, 1f, 0f});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
	}


	//Logic for drawing the frequency values into a texture and shifting the contents of the texture right each frame.
	//Two pixmaps are needed and the contents are copied between them to shift the texture one pixel each frame.
	Pixmap pixmap1;
	Pixmap pixmap2;
	Texture texture;

	final int width = 256;
	final int height = SPECTRUM_LENGTH;

	/**
	 * Updates the texture each frame with new data and shifts the contents right.
	 * @param data
	 */
	private void updatePixmap(float[] data) {
		Pixmap read;
		Pixmap write;
		if (graphics.getFrameId() % 2 == 0) {
			read = pixmap1;
			write = pixmap2;
		} else {
			read = pixmap2;
			write = pixmap1;
		}
		//Draw contents of read to write, shifted by one pixel to the right
		write.drawPixmap(read, 0, 0, width - 1, height, 1, 0, width - 1, height);
		//Draw new data into the left edge of write
		for (int i = 0; i < SPECTRUM_LENGTH; i++) {
			write.drawPixel(0, i, Color.rgba8888(data[i], 0f, 0f, 1f));
		}
		//Upload the updated pixmap to the texture
		texture.draw(write, 0, 0);
		texture.bind(0);
	}

	/**
	 * Initialize pixmaps and texture
	 */
	private void createPixmapTexture() {
		pixmap1 = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pixmap2 = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		texture = new Texture(width, height, Pixmap.Format.RGBA8888);
	}
}
