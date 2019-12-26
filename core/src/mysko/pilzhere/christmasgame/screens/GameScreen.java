package mysko.pilzhere.christmasgame.screens;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import mysko.pilzhere.christmasgame.ChristmasGame;
import mysko.pilzhere.christmasgame.Utils;
import mysko.pilzhere.christmasgame.entities.Comet;
import mysko.pilzhere.christmasgame.entities.Entity;
import mysko.pilzhere.christmasgame.entities.Island;
import mysko.pilzhere.christmasgame.entities.Player;
import mysko.pilzhere.christmasgame.entities.Seawave;
import mysko.pilzhere.christmasgame.entities.SpriteDirection;
import mysko.pilzhere.christmasgame.entities.Tree;
import mysko.pilzhere.christmasgame.entities.enemies.Yeti;
import mysko.pilzhere.christmasgame.entities.items.Candycane;

public class GameScreen implements Screen {
	public final ChristmasGame game;

	private boolean debugRenderShapes;

	public final AssetManager assMan;
	private final SpriteBatch batch;
	private final ModelBatch mdlBatch;

	private ShapeRenderer shapeRenderer;

	public OrthographicCamera cam;
	public Viewport viewport;

	public Array<Entity> entities = new Array<Entity>();
	private Island island;

	private Player player;

	public void setPlayer(Player player) {
		if (this.player == null)
			this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

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

		viewport = new FitViewport(Gdx.graphics.getWidth() / game.getWindowScale(),
				Gdx.graphics.getHeight() / game.getWindowScale(), cam); // original
//		viewport = new FillViewport(1280, 720, cam);
//		viewport = new StretchViewport(854, 480, cam);

		game.setWindowScale(5);
		resizeWindow();

		island = new Island(this, Vector3.Zero);

		final Vector3 randomPosVector = new Vector3();

		for (int i = 0; i < 50; i++) {
			entities.add(new Tree(this, new Vector3(MathUtils.random(-35, 35), 0, MathUtils.random(-35, 35))));

			float randomDist = 0f;
			while (randomDist < 60) {
				randomPosVector.set(getRandom(), 0, getRandom());
				randomDist = randomPosVector.dst(Vector3.Zero);
			}

			entities.add(new Seawave(this, new Vector3(randomPosVector.x, 0, randomPosVector.z)));
		}

		entities.add(new Yeti(this, new Vector3(30, 0, 30)));

		entities.add(new Comet(this, new Vector3(MathUtils.random(-100, 100), 50, MathUtils.random(-100, 100))));
	}

	private float getRandom() {
		final float random = MathUtils.random(-80, 80);
		return random;
	}

	public Island getIsland() {
		return island;
	}

	@Override
	public void show() {

	}

	private boolean lmbPressed;
	private boolean rmbPressed;
	private Rectangle touchRect = new Rectangle(0, 0, 5, 5);
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

		if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
			game.setWindowScale(game.getWindowScale() + game.stdWindowScaleIncrement);
			resizeWindow();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
			game.setWindowScale(game.getWindowScale() - game.stdWindowScaleIncrement);
			resizeWindow();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			debugRenderShapes = !debugRenderShapes;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			camSpin = !camSpin;
		}

//		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//			entities.add(new Comet(this, new Vector3(MathUtils.random(-75, 75), 50, MathUtils.random(-75, 75))));
//		}

		if (player != null) {
			// TODO: If player is too far from middle = moves out while moving.
			// Temporary solution is slower island rotation speed.
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				player.moveLeft(delta);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				player.moveRight(delta);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				player.moveUp(delta);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				player.moveDown(delta);
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
				if (!player.chopping) {
					player.chop(delta);

					switch (player.spriteDir) {
					case UP:
						touchRect.setPosition(player.screenPos.x,
								(player.screenPos.y + Gdx.graphics.getHeight() / 28f));
						break;
					case DOWN:
						touchRect.setPosition(player.screenPos.x,
								(player.screenPos.y - Gdx.graphics.getHeight() / 24f));
						break;
					case LEFT:
						touchRect.setPosition(player.screenPos.x - (Gdx.graphics.getWidth() / 42f), player.screenPos.y);
						break;
					case RIGHT:
						touchRect.setPosition(player.screenPos.x + (Gdx.graphics.getWidth() / 50f), player.screenPos.y);
						break;
					}

					for (Entity ent : entities) {
						if (ent.rect != null) {
							if (touchRect.overlaps(ent.rect)) {
								ent.onChop(delta);
								break; // Can only touch one object at a time.
							}
						}
					}
				}
			}
		}

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
					touchRect.setPosition(mousePos2D.x - touchRect.getWidth() / 2,
							(Gdx.graphics.getHeight() - mousePos3DUnprojected.y) - (touchRect.getHeight() / 2));

					for (Entity ent : entities) {
						if (ent.rect != null) {
							if (touchRect.overlaps(ent.rect)) {
								ent.onTouch(delta);
								break; // Can only touch one object at a time.
							}
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

	private final int camRotSpeed = 3; // 25

	private void tick(float delta) {
		rotateCamAroundIsland(cam, camRotSpeed * delta);
		cam.lookAt(Vector3.Zero);
		cam.update();

		island.tick(delta);

		if (player != null)
			player.checkForOverlaps(delta);

		for (Entity ent : entities) {
			if (ent.destroy) {
				ent.destroy();
				entities.removeValue(ent, true);
			}
		}

		for (Entity ent : entities) {
			ent.tick(delta);
		}

		entities.sort(comparatorRenderOrder);
	}

	private boolean camSpin = false;

	private void rotateCamAroundIsland(OrthographicCamera cam, float speed) {
		if (camSpin)
			cam.rotateAround(Vector3.Zero, Vector3.Y, speed);
	}

//	Sorts render order!
	Comparator<Entity> comparatorRenderOrder = new Comparator<Entity>() {
		@Override
		public int compare(Entity ent1, Entity ent2) {
			if (ent1.distFromCam > ent2.distFromCam) {
				return -1;
			} else if (ent1.distFromCam < ent2.distFromCam) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	private void resizeWindow() {
		Gdx.graphics.setWindowedMode((int) (game.stdWindowWidth * game.getWindowScale()),
				(int) (game.stdWindowHeight * game.getWindowScale()));
	}

	@Override
	public void render(float delta) {
		input(delta);
		tick(delta);

		mdlBatch.begin(cam);
		island.render3D(mdlBatch, delta);
		mdlBatch.end();

		batch.setProjectionMatrix(cam.projection.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		batch.begin();
		for (Entity ent : entities) {
			ent.render2D(batch, delta);
		}

		if (debugRenderShapes) {
			if (shapeRenderer == null)
				shapeRenderer = new ShapeRenderer();

			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rect(touchRect.x, touchRect.y, touchRect.getWidth(), touchRect.getHeight());

			shapeRenderer.setColor(Color.ORANGE);
			for (Entity ent : entities) {
				if (ent.rect != null) {
					if (ent.shapeColor != null)
						shapeRenderer.setColor(ent.shapeColor);
					shapeRenderer.rect(ent.rect.getX(), ent.rect.getY(), ent.rect.getWidth(), ent.rect.getHeight());
				}
			}
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.end();
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
