package mysko.pilzhere.christmasgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import mysko.pilzhere.christmasgame.ChristmasGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.foregroundFPS = 144;
		config.backgroundFPS = 60;
		config.title = "ChristmasGame";
		config.fullscreen = false;
		config.resizable = true;
		config.width = 1280;
		config.height = 720;
		config.initialBackgroundColor = Color.WHITE;
		config.samples = 0;
		config.vSyncEnabled = false;
//		config.addIcon(path, fileType);
		
		new LwjglApplication(new ChristmasGame(), config);
	}
}
