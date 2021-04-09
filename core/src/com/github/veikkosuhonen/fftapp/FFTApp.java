package com.github.veikkosuhonen.fftapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.veikkosuhonen.fftapp.audio.SoundPlayer;
import com.github.veikkosuhonen.fftapp.fft.utils.ArrayUtils;

import java.util.Arrays;

import static com.badlogic.gdx.Gdx.*;

public class FFTApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	ShaderProgram shader;
	Mesh mesh;

	SoundPlayer player;

	int BUFFER_SIZE = 512;
	int CHUNK_SIZE = 64;

	long startTime;

	int renderDataLength = 256;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		shader = new ShaderProgram(Gdx.files.internal("test.vert"), Gdx.files.internal("test.frag"));
		shader.bind();
		System.out.println(Arrays.toString(shader.getUniforms()));
		System.out.println(shader.getFragmentShaderSource());
		mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
		mesh.setVertices(new float[]
				{-1f, -1f, 0f,
				1f, -1f, 0f,
				1f, 1f, 0f,
				-1f, 1f, 0f});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});

		player = new SoundPlayer("C:\\Users\\Veikko\\IdeaProjects\\sound\\Crystallized.wav", CHUNK_SIZE, BUFFER_SIZE, 60);
		player.start();

		startTime = System.currentTimeMillis();
	}

	@Override
	public void render () {
		long time = System.currentTimeMillis();

		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		double[][] data = player.getLeftRightDFT();
		float[] dataL = ArrayUtils.slice(ArrayUtils.toFloatArray(data[0]), renderDataLength);
		float[] dataR = ArrayUtils.slice(ArrayUtils.toFloatArray(data[1]), renderDataLength);
		float[] dataLR = ArrayUtils.join(dataL, dataR);

		shader.bind();
		shader.setUniformf("u_time", (time - startTime) * 1f / 1000);
		shader.setUniform1fv("u_freq", dataLR, 0, renderDataLength * 2);
		shader.setUniformf("u_resolution", graphics.getWidth(), graphics.getHeight());
		mesh.render(shader, GL20.GL_TRIANGLES);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		shader.dispose();
		mesh.dispose();

	}

	private float[] preprocess(double[] data) {
		int n = data.length < 512 ? data.length : 511;
		//logarithmic scaling, down to length of renderDataLength
		double a = Math.log(n) / Math.log(renderDataLength);
		float[] result = new float[renderDataLength];
		for (int i = 0; i < renderDataLength; i++) {
			result[i] = (float) data[(int) Math.pow(i, a) + 1];
		}
		return result;
	}
}
