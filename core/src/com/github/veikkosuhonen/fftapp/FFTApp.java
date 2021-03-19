package com.github.veikkosuhonen.fftapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.veikkosuhonen.fftapp.fft.FFT;
import com.github.veikkosuhonen.fftapp.fft.MockFFT;

public class FFTApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	FFT fft;

	ShaderProgram shader;
	Mesh mesh;

	AudioRecorder recorder;
	AudioDevice device;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		fft = new MockFFT();

		shader = new ShaderProgram(Gdx.files.internal("test.vert"), Gdx.files.internal("test.frag"));
		mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
		mesh.setVertices(new float[]
				{-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});

		Thread audio = new Thread() {
			@Override
			public void run() {
				recorder = Gdx.audio.newAudioRecorder(22050, true);
				device = Gdx.audio.newAudioDevice(22050, true);
				while (true) {
					short[] pcm = new short[1024];
					recorder.read(pcm, 0, pcm.length);
					device.writeSamples(pcm, 0, pcm.length);
				}
			}
		};
		audio.start();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shader.bind();
		mesh.render(shader, GL20.GL_TRIANGLES);



	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		shader.dispose();
		mesh.dispose();

	}
}
