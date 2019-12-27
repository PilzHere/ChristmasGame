package mysko.pilzhere.christmasgame.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import mysko.pilzhere.christmasgame.entities.Player;
import mysko.pilzhere.christmasgame.screens.GameScreen;

public class GUI {
	private final GameScreen screen;
	private final SpriteBatch batch;
	public final BitmapFont font01_32; // get-set
	public final BitmapFont font01_16; // get-set
	public BitmapFont currentFont; // get set

	private Player player;

	public Array<GUIEntity> guiEnts = new Array<GUIEntity>(); // get set

	private HeartIcon hI1;
	private HeartIcon hI2;
	private HeartIcon hI3;
	private HeartIcon hI4;
	private HeartIcon hI5;

	public GUI(GameScreen screen) {
		this.screen = screen;
		this.batch = screen.game.getBatch();

		font01_32 = screen.assMan.get("fonts/font01_32.fnt");
		font01_16 = screen.assMan.get("fonts/font01_16.fnt");

		if (screen.game.windowScale > 3.5f) {
			currentFont = font01_32;
		} else {
			currentFont = font01_16;
		}

		player = screen.getPlayer();

		guiEnts.add(new LogsIcon(screen, this, new Vector2(8, 8)));
		guiEnts.add(new CandyIcon(screen, this, new Vector2(128, 8)));

		hI1 = new HeartIcon(screen, this, new Vector2(256 + (32 * 1), 8));
		hI2 = new HeartIcon(screen, this, new Vector2(256 + (32 * 2), 8));
		hI3 = new HeartIcon(screen, this, new Vector2(256 + (32 * 3), 8));
		hI4 = new HeartIcon(screen, this, new Vector2(256 + (32 * 4), 8));
		hI5 = new HeartIcon(screen, this, new Vector2(256 + (32 * 5), 8));

		guiEnts.add(hI1);
		guiEnts.add(hI2);
		guiEnts.add(hI3);
		guiEnts.add(hI4);
		guiEnts.add(hI5);

		System.out.println("GUI added!");
	}

	public void tick(float delta) {
		player = screen.getPlayer();

		if (screen.game.windowScale > 3.5f) {
			currentFont = font01_32;
		} else {
			currentFont = font01_16;
		}

		if (player != null) {
			switch (player.hp) {
			case 0:
				hI1.drawLeft = false;
				hI1.drawRight = false;
				hI2.drawLeft = false;
				hI2.drawRight = false;
				hI3.drawLeft = false;
				hI3.drawRight = false;
				hI4.drawLeft = false;
				hI4.drawRight = false;
				hI5.drawLeft = false;
				hI5.drawRight = false;
				break;
			case 1:
				hI1.drawLeft = true;
				hI1.drawRight = true;
				hI2.drawLeft = false;
				hI2.drawRight = false;
				hI3.drawLeft = false;
				hI3.drawRight = false;
				hI4.drawLeft = false;
				hI4.drawRight = false;
				hI5.drawLeft = false;
				hI5.drawRight = false;
				break;
			case 2:
				hI1.drawLeft = true;
				hI1.drawRight = true;
				hI2.drawLeft = true;
				hI2.drawRight = true;
				hI3.drawLeft = false;
				hI3.drawRight = false;
				hI4.drawLeft = false;
				hI4.drawRight = false;
				hI5.drawLeft = false;
				hI5.drawRight = false;
				break;
			case 3:
				hI1.drawLeft = true;
				hI1.drawRight = true;
				hI2.drawLeft = true;
				hI2.drawRight = true;
				hI3.drawLeft = true;
				hI3.drawRight = true;
				hI4.drawLeft = false;
				hI4.drawRight = false;
				hI5.drawLeft = false;
				hI5.drawRight = false;
				break;
			case 4:
				hI1.drawLeft = true;
				hI1.drawRight = true;
				hI2.drawLeft = true;
				hI2.drawRight = true;
				hI3.drawLeft = true;
				hI3.drawRight = true;
				hI4.drawLeft = true;
				hI4.drawRight = true;
				hI5.drawLeft = false;
				hI5.drawRight = false;
				break;
			case 5:
				hI1.drawLeft = true;
				hI1.drawRight = true;
				hI2.drawLeft = true;
				hI2.drawRight = true;
				hI3.drawLeft = true;
				hI3.drawRight = true;
				hI4.drawLeft = true;
				hI4.drawRight = true;
				hI5.drawLeft = true;
				hI5.drawRight = true;
				break;
			}
		}

		for (GUIEntity guiEnt : guiEnts) {
			guiEnt.tick(delta);
		}
	}

	public void render2D(float delta) {
		for (GUIEntity guiEnt : guiEnts) {
			guiEnt.render2D(batch, delta);
		}
	}

	public void destroy() {
		guiEnts.clear();
	}
}
