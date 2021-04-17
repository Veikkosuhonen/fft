package com.github.veikkosuhonen.fftapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.veikkosuhonen.fftapp.audio.SoundPlayer;
import com.github.veikkosuhonen.fftapp.fft.utils.ArrayUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static com.badlogic.gdx.Gdx.*;

public class FFTApp extends ApplicationAdapter {

	final int CHUNK_SIZE = 128;
	final int QUEUE_LENGTH = 360;
	final int FPS = 60;
	final int WINDOW = 64;

	SoundPlayer player;
	File audioFile;

	final int SPECTRUM_LENGTH = 400;

	ShaderProgram shader;
	Mesh mesh;
	long startTime;

	/**
	 * Called when GDX application starts
	 */
	@Override
	public void create() {
		chooseDefaultFile();
		if (audioFile == null) {
			chooseFile();
		}
		initializeGraphics();

		player = new SoundPlayer(
				audioFile,
				CHUNK_SIZE,
				QUEUE_LENGTH,
				WINDOW,
				FPS);
		player.start();

		startTime = System.currentTimeMillis();
	}

	/**
	 * Called each frame
	 */
	@Override
	public void render() {
		long time = System.currentTimeMillis();

		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		double[][] data = player.getLeftRightDCT();
		float[] dataL = processFrequencyData(data[0]);
		float[] dataR = processFrequencyData(data[1]);
		float[] dataLR = ArrayUtils.join(dataL, dataR);

		shader.bind();
		shader.setUniformf("u_time", (time - startTime) * 1f / 1000);
		shader.setUniform1fv("u_freq", dataLR, 0, SPECTRUM_LENGTH * 2);
		shader.setUniformf("u_resolution", graphics.getWidth(), graphics.getHeight());
		mesh.render(shader, GL20.GL_TRIANGLES);
	}
	
	@Override
	public void dispose() {
		shader.dispose();
		mesh.dispose();
	}

	/**
	 * @param data
	 * @return
	 */
	private float[] processFrequencyData(double[] data) {
		if (data.length < SPECTRUM_LENGTH) {
			throw new IllegalArgumentException("DCT data should not be less than SPECTRUM_LENGTH (" + data.length + " < " + SPECTRUM_LENGTH + ")");
		}
		float[] result = ArrayUtils.abs(ArrayUtils.toFloatArray(ArrayUtils.slice(data, SPECTRUM_LENGTH)));
		//float mean = ArrayUtils.select(result, 0, SPECTRUM_LENGTH - 1, SPECTRUM_LENGTH / 2);

		//Smooth
		for (int i = 1; i < SPECTRUM_LENGTH - 1; i++) {
			result[i] = (result[i - 1] + 2 * result[i] + result[i + 1]) / 4;
		}
		return result;
	}

	/**
	 * Compile shader program and create the screen draw quad
	 */
	private void initializeGraphics() {
		shader = new ShaderProgram(Gdx.files.internal("test.vert"), Gdx.files.internal("test.frag"));
		shader.bind();

		mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
		mesh.setVertices(new float[]
					{-1f, -1f, 0f,
					1f, -1f, 0f,
					1f, 1f, 0f,
					-1f, 1f, 0f});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
	}

	/**
	 * Shows a file chooser dialog until a WAV file is chosen
	 */
	private void chooseFile() {
		JPanel panel = new JPanel();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("Select");
		fileChooser.setDialogTitle("Select a WAV audio file to be played");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV files", "wav");
		fileChooser.setFileFilter(filter);
		while (true) {
			if (fileChooser.showDialog(panel, "Select") == JFileChooser.APPROVE_OPTION) {
				audioFile = fileChooser.getSelectedFile();
				break;
			}
		}
	}

	/**
	 * By setting the environment variable AUDIO_FILE to a file path, you can skip the file chooser dialog
	 */
	private void chooseDefaultFile() {
		String path = System.getenv("AUDIO_FILE");
		if (path != null) {
			audioFile = new File(path);
		}
		if (!audioFile.exists()) {
			audioFile = null;
		}
	}
}
