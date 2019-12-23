package mysko.pilzhere.christmasgame.screens;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import mysko.pilzhere.christmasgame.ChristmasGame;
import mysko.pilzhere.christmasgame.Utils;
import mysko.pilzhere.christmasgame.entities.Candycane;
import mysko.pilzhere.christmasgame.entities.Comet;
import mysko.pilzhere.christmasgame.entities.Entity;
import mysko.pilzhere.christmasgame.entities.Island;
import mysko.pilzhere.christmasgame.entities.Tree;

public class GameScreen implements Screen {
	private ChristmasGame game;

	public final AssetManager assMan;
	private final SpriteBatch batch;
	private final ModelBatch mdlBatch;

	public OrthographicCamera cam;
	public FitViewport viewport;

	public Array<Entity> entities = new Array<Entity>();
	private Island island;

	public GameScreen(ChristmasGame game) {
		this.game = game;
		this.assMan = game.getAssMan();
		this.batch = game.getBatch();
		this.mdlBatch = game.getMdlBatch();
		
		Utils.setScreen(this); // Never forget...

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 0.01f;
		cam.far = 300f;
		cam.position.set(0, 100, 175); // 0 1 0
		cam.lookAt(Vector3.Zero);
		cam.zoom = 0.5f;
		cam.update();

		viewport = new FitViewport(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4, cam);

		island = new Island(this, Vector3.Zero);
		
		for (int i = 0; i < 50; i++) {
			entities.add(new Tree(this, new Vector3(MathUtils.random(-35, 35), 0, MathUtils.random(-35, 35))));
		}
		
		entities.add(new Comet(this, new Vector3(MathUtils.random(-100, 100), 50, MathUtils.random(-100, 100))));
	}

	@Override
	public void show() {

	}

	private boolean lmbPressed;
	private boolean rmbPressed;
	private Rectangle touchRect = new Rectangle();
	private Ray ray;

	private final Vector3 mousePos3D = new Vector3();
	private final Vector3 mousePos3DUnprojected = new Vector3();
	private final Vector2 mousePos2D = new Vector2();

	private void input(float delta) {
		mousePos3D.x = Gdx.input.getX();
		mousePos3D.y = Gdx.input.getY();
		mousePos3D.z = 0;
		mousePos3DUnprojected.set(mousePos3D.cpy());
		cam.unproject(mousePos3D.cpy());
		mousePos2D.x = mousePos3D.x;
		mousePos2D.y = mousePos3D.y;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			camSpin = !camSpin;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			entities.add(new Comet(this, new Vector3(MathUtils.random(-75, 75), 50, MathUtils.random(-75, 75))));
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
		
		leftMouseButton(delta);
		rightMouseButton(delta);
	}
	
	private void leftMouseButton(float delta) {
		if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			lmbPressed = false;
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			if (!lmbPressed) {
				if (Gdx.input.justTouched()) {
					touchRect.setPosition(mousePos2D.x, Gdx.graphics.getHeight() - mousePos3DUnprojected.y);

					for (Entity ent : entities) {
						if (touchRect.overlaps(ent.rect)) {
							ent.onTouch(delta);
							break; // Can only touch one at a time.
						}
					}

					lmbPressed = true;
				}
			}
		}
	}
	
	private void rightMouseButton(float delta) {
		if (!Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			rmbPressed = false;
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			if (!rmbPressed) {
				if (Gdx.input.justTouched()) {
					ray = cam.getPickRay(mousePos2D.x, mousePos2D.y);

					placeEntityOnCursor();

					rmbPressed = true;
				}
			}
		}
	}

	private final Vector3 placePos = new Vector3();

	private void placeEntityOnCursor() {
		if (Intersector.intersectRayBoundsFast(ray, island.bb)) {
			float distance = -ray.origin.y / ray.direction.y;

			placePos.set(ray.direction).scl(distance).add(ray.origin);

			entities.add(new Candycane(this, placePos.cpy()));
		}
	}

	private final int camRotSpeed = 14; //25

	private void tick(float delta) {
		rotateCamAroundIsland(cam, camRotSpeed * delta);
		cam.lookAt(Vector3.Zero);
		cam.update();

		island.tick(delta);
		
		for (Entity ent : entities) {
			if (ent.destroy) {
				ent.destroy();
				entities.removeValue(ent, true);
			}
		}

		for (Entity ent : entities) {
			ent.tick(delta);
		}
		
		entities.sort(comparator); // sort sprite render order!
	}

	private boolean camSpin = false;

	private void rotateCamAroundIsland(OrthographicCamera cam, float speed) {
		if (camSpin)
			cam.rotateAround(Vector3.Zero, Vector3.Y, speed);
	}

//	Sorts render order!
	Comparator<Entity> comparator = new Comparator<Entity>() {
		@Override
		public int compare(Entity ent1, Entity ent2) {
//			if (o1 instanceof Door || o2 instanceof Door) {
//				return 1; // keep doors rendering first OLD
//			} else {
			if (ent1.distFromCam > ent2.distFromCam) {
				return -1;
			} else if (ent1.distFromCam < ent2.distFromCam) {
				return 1;
			} else {
				return 0;
			}
			// }
		}
	};

	@Override
	public void render(float delta) {
		input(delta);
		tick(delta);

		mdlBatch.begin(cam);
		island.render3D(mdlBatch, delta);
		mdlBatch.end();

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
