package com.github.veikkosuhonen.fftapp.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.veikkosuhonen.fftapp.FFTApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		//config.vSyncEnabled = true;
		//config.useGL30 = true;
		new LwjglApplication(new FFTApp(), config);
	}
}
