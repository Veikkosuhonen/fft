package com.github.veikkosuhonen.fftapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.veikkosuhonen.fftapp.audio.SoundPlayer;
import com.github.veikkosuhonen.fftapp.fft.DFT;
import com.github.veikkosuhonen.fftapp.fft.NaiveDFT;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static com.badlogic.gdx.Gdx.*;

public class FFTApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	DFT fft;

	ShaderProgram shader;
	Mesh mesh;

	SoundPlayer player;

	int BUFFER_SIZE = 1024;
	float[] data;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		fft = new NaiveDFT();

		shader = new ShaderProgram(Gdx.files.internal("test.vert"), Gdx.files.internal("test.frag"));
		mesh = new Mesh(true, 4, 6, VertexAttribute.Position());
		mesh.setVertices(new float[]
				{-1f, -1f, 0f,
				1f, -1f, 0f,
				1f, 1f, 0f,
				-1f, 1f, 0f});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});

		player = new SoundPlayer("C:\\Users\\Veikko\\IdeaProjects\\sound\\Crystallized.wav", BUFFER_SIZE);
		player.start();
	}

	@Override
	public void render () {
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shader.bind();
		shader.setUniform1fv("data", player.getDFT(), 0, BUFFER_SIZE);
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
}
