package mysko.pilzhere.christmasgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import mysko.pilzhere.christmasgame.screens.GameScreen;

public class ChristmasGame extends Game {

	/*
	 * TODO the theme is "The world is food!" and I have no idea what to do make...
	 * * Spinning planet/island? * Mode7ish race game? * Doom clone? * 2D
	 * platformer?
	 */

	private SpriteBatch batch;
	private ModelBatch mdlBatch;
	private AssetManager assMan;

	@Override
	public void create() {
		assMan = new AssetManager();
		batch = new SpriteBatch();
		mdlBatch = new ModelBatch();

		setScreen(new GameScreen(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(48 / 255f, 96 / 255f, 130 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		getScreen().render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose() {
		getScreen().dispose();

		batch.dispose();
		mdlBatch.dispose();

		assMan.dispose();
	}

	public AssetManager getAssMan() {
		return assMan;
	}

	public ModelBatch getMdlBatch() {
		return mdlBatch;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
