package mysko.pilzhere.christmasgame;

import com.badlogic.gdx.math.Vector3;

import mysko.pilzhere.christmasgame.screens.GameScreen;

public class Utils {
	private static GameScreen screen;
	
	public static void setScreen(GameScreen screen) {
		Utils.screen = screen;
	}
	
	private static Vector3 tempScreenPosition = new Vector3();
	public static Vector3 calculateScreenPosition(Vector3 position, Vector3 projPos) {		
		projPos.set(screen.viewport.project(position.cpy()));
		tempScreenPosition.set(projPos.x, projPos.y, 0);
		
		return tempScreenPosition.cpy();
		
//		Old projPos and ScreenPos for trees and candycanes etc...
//		projPos.set(screen.viewport.project(position.cpy()));
//		screenPos.set(projPos.x, projPos.y, 0);
	}
	
}
