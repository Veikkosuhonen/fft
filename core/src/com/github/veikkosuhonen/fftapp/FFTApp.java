package com.github.veikkosuhonen.fftapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.veikkosuhonen.fftapp.audio.SoundPlayer;
import com.github.veikkosuhonen.fftapp.fft.DFT;

public class FFTApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	DFT fft;

	ShaderProgram shader;
	Mesh mesh;

	SoundPlayer player;


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

		player = new SoundPlayer("C:\\Users\\Veikko\\IdeaProjects\\sound\\Crystallized.wav", 1024);
		player.start();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		img.bind();
		shader.bind();
		shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
