package mysko.pilzhere.christmasgame.screens;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import mysko.pilzhere.christmasgame.ChristmasGame;
import mysko.pilzhere.christmasgame.entities.Entity;
import mysko.pilzhere.christmasgame.entities.Island;
import mysko.pilzhere.christmasgame.entities.Tree;

public class GameScreen implements Screen {
	private ChristmasGame game;

	private final AssetManager assMan;
	private final SpriteBatch batch;
	private final ModelBatch mdlBatch;

	public OrthographicCamera cam;
	public FitViewport viewport;

	public Array<Entity> entities = new Array<Entity>();	
	private Island island;
	
	private Texture texSpriteTest;
	private Sprite spriteTest;

	public GameScreen(ChristmasGame game) {
		this.game = game;
		this.assMan = game.getAssMan();
		this.batch = game.getBatch();
		this.mdlBatch = game.getMdlBatch();

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 0.01f;
		cam.far = 300f;
		cam.position.set(0, 100, 120); // 0 1 0
		cam.lookAt(Vector3.Zero);
		cam.zoom = 0.5f;
		cam.update();

		viewport = new FitViewport(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4, cam);

		texSpriteTest = new Texture("sprite01.png");
		spriteTest = new Sprite(texSpriteTest);
		
		island = new Island(this, Vector3.Zero);
		entities.add(new Tree(this, Vector3.Zero));
	}

	@Override
	public void show() {

	}

	private float mouseX;
	private float mouseY;

	private boolean leftWasPressed;
	
	private void input(float delta) {		
//		ESC
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			camSpin = !camSpin;
		}

//		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//			cam.translate(0, 0, -1);
//		}
//
//		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//			cam.translate(0, 0, 1);
//		}
//
//		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//			cam.translate(-1, 0, 0);
//		}
//
//		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//			cam.translate(1, 0, 0);
//		}
		
		if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			leftWasPressed = false;
		}
		
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			if (!leftWasPressed) {
				mouseX = Gdx.input.getX();
				mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
				
				entities.add(new Tree(this, new Vector3(MathUtils.random(-45, 45), 0, MathUtils.random(-45, 45))));
				
				leftWasPressed = true;
			}
		}
	}

	private final int camRotSpeed = 25;

	private void tick(float delta) {
		rotateCamAroundIsland(cam, camRotSpeed * delta);
		cam.lookAt(Vector3.Zero);		
		cam.update();

		island.tick(delta);
		
		for (Entity ent : entities) {
			ent.tick(delta);
		}
	}
	
	public boolean camSpin = true;

	private void rotateCamAroundIsland(OrthographicCamera cam, float speed) {
		if (camSpin)
			cam.rotateAround(Vector3.Zero, Vector3.Y, speed);
	}

//	Sorts render order!
	Comparator<Entity> comparator = new Comparator<Entity>() {
		@Override
		public int compare(Entity ent1, Entity ent2) {
//			if (o1 instanceof Door || o2 instanceof Door) {
//				return 1; // keep doors rendering first
//			
//			} else {
				if (ent1.distFromCam > ent2.distFromCam) {
					return -1;
				} else if (ent1.distFromCam < ent2.distFromCam) {
					return 1;
				} else {
					return 0;
				}
			//}
		}
	};
	
	@Override
	public void render(float delta) {
		input(delta);
		tick(delta);

		entities.sort(comparator); // sort render order!
		
		mdlBatch.begin(cam);
		island.render3D(mdlBatch, delta);
		mdlBatch.end();

//		sprite.setX(mouseX);
//		sprite.setY(mouseY);

		batch.begin();
		for (Entity ent : entities) {
			ent.render2D(batch, delta);
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		island.dispose();
	}
}
